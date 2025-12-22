package com.hoho.leave.domain.user.service;

import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.repository.GradeRepository;
import com.hoho.leave.domain.org.repository.PositionRepository;
import com.hoho.leave.domain.org.repository.TeamRepository;
import com.hoho.leave.domain.user.dto.request.UserJoinRequest;
import com.hoho.leave.domain.user.dto.request.UserUpdateRequest;
import com.hoho.leave.domain.user.dto.response.UserDetailResponse;
import com.hoho.leave.domain.user.dto.response.UserListResponse;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 서비스.
 * <p>
 * 사용자 계정의 생성, 조회, 수정, 삭제 등 비즈니스 로직을 처리한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PositionRepository positionRepository;
    private final GradeRepository gradeRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 사용자를 생성한다.
     *
     * @param request 회원가입 요청 정보
     * @return 생성된 사용자 엔티티
     */
    public User createUser(UserJoinRequest request) {
        checkDuplicateEmail(request.getEmail());
        User user = User.create(request, passwordEncoder);
        assignOrgIfPresent(request.getTeamName(), request.getPositionName(), request.getGradeName(), user);
        return userRepository.save(user);
    }

    /**
     * 사용자 상세 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 상세 정보 응답
     */
    @Transactional(readOnly = true)
    public UserDetailResponse getUser(Long userId) {
        User user = getUserEntity(userId);
        return UserDetailResponse.of(user);
    }

    /**
     * 사용자 목록을 페이지 단위로 조회한다.
     *
     * @param size 페이지당 항목 수
     * @param page 페이지 번호
     * @return 사용자 목록 응답
     */
    @Transactional(readOnly = true)
    public UserListResponse getAllUsers(Integer size, Integer page) {
        Pageable pageable = getPageable(size, page);

        Page<User> pageList = userRepository.findPageWithOrg(pageable);

        List<UserDetailResponse> list =
                pageList.getContent()
                        .stream()
                        .map(UserDetailResponse::of)
                        .toList();

        return UserListResponse.of(pageList, list);
    }

    /**
     * 사용자 정보를 수정한다.
     *
     * @param userId 사용자 ID
     * @param request 수정할 사용자 정보
     */
    @Transactional
    public void updateUser(Long userId, UserUpdateRequest request) {
        User user = getUserEntity(userId);
        assignOrgIfPresent(request.getTeamName(), request.getPositionName(), request.getGradeName(), user);
        user.updatePassword(request.getPassword(), passwordEncoder);
        user.updateUsername(request.getUsername());
    }

    /**
     * 사용자를 삭제한다.
     *
     * @param userId 사용자 ID
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserEntity(userId);
        userRepository.delete(user);
    }

    /**
     * 사용자 엔티티를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     */
    public User getUserEntity(Long userId) {
        return userRepository.findByIdWithOrg(userId)
                .orElseThrow(() -> new NotFoundException("Not Found User : " + userId));
    }

    /**
     * 사용자 엔티티 목록을 조회한다.
     *
     * @param userIds 사용자 ID 목록
     * @return 사용자 엔티티 목록
     */
    public List<User> getUserEntityList(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    /**
     * 페이지 정보를 생성한다.
     *
     * @param size 페이지당 항목 수
     * @param page 페이지 번호
     * @return 페이지 요청 객체
     */
    private PageRequest getPageable(Integer size, Integer page) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("hireDate")));
    }

    /**
     * 이메일 중복을 체크한다.
     *
     * @param email 이메일
     */
    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException("Duplicate Email : " + email);
        }
    }

    /**
     * 조직 정보를 사용자에게 할당한다.
     *
     * @param teamName 부서명
     * @param positionName 직책명
     * @param gradeName 직급명
     * @param user 사용자 엔티티
     */
    private void assignOrgIfPresent(String teamName, String positionName, String gradeName, User user) {
        if (teamName != null) assignTeam(teamName, user);

        if (positionName != null) assignPosition(positionName, user);

        if (gradeName != null) assignGrade(gradeName, user);
    }

    /**
     * 부서를 사용자에게 할당한다.
     *
     * @param teamName 부서명
     * @param user 사용자 엔티티
     */
    private void assignTeam(String teamName, User user) {
        Team team = teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new NotFoundException("Not Found Team : " + teamName));
        user.assignTeam(team);
    }

    /**
     * 직책을 사용자에게 할당한다.
     *
     * @param positionName 직책명
     * @param user 사용자 엔티티
     */
    private void assignPosition(String positionName, User user) {
        Position position = positionRepository.findByPositionName(positionName)
                .orElseThrow(() -> new NotFoundException("Not Found Position : " + positionName));
        user.assignPosition(position);
    }

    /**
     * 직급을 사용자에게 할당한다.
     *
     * @param gradeName 직급명
     * @param user 사용자 엔티티
     */
    private void assignGrade(String gradeName, User user) {
        Grade grade = gradeRepository.findByGradeName(gradeName)
                .orElseThrow(() -> new NotFoundException("Not Found Grade : " + gradeName));
        user.assignGrade(grade);
    }
}
