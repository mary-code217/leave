package com.hoho.leave.domain.user.service;

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
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PositionRepository positionRepository;
    private final GradeRepository gradeRepository;

    private final PasswordEncoder passwordEncoder;

    public User createUser(UserJoinRequest request) {
        checkDuplicateEmail(request.getEmail());
        User user = User.create(request, passwordEncoder);
        assignOrgIfPresent(request.getTeamName(), request.getPositionName(), request.getGradeName(), user);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUser(Long userId) {
        User user = getUserEntity(userId);
        return UserDetailResponse.of(user);
    }

    @Transactional(readOnly = true)
    public UserListResponse getAllUsers(Integer size, Integer page) {
        Pageable pageable =
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("hireDate")));

        Page<User> users = userRepository.findPageWithOrg(pageable);

        List<UserDetailResponse> list =
                users.getContent()
                        .stream()
                        .map(UserDetailResponse::of)
                        .toList();

        return UserListResponse.of(
                page,
                size,
                list,
                users.getTotalPages(),
                users.getTotalElements(),
                users.isFirst(),
                users.isLast()
        );
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateRequest request) {
        User user = getUserEntity(userId);
        assignOrgIfPresent(request.getTeamName(), request.getPositionName(), request.getGradeName(), user);
        user.updatePassword(request.getPassword(), passwordEncoder);
        user.updateUsername(request.getUsername());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserEntity(userId);
        userRepository.delete(user);
    }

    /**
     * 유저 엔티티 조회
     */
    public User getUserEntity(Long userId) {
        return userRepository.findByIdWithOrg(userId)
                .orElseThrow(() -> new NotFoundException("Not Found User : " + userId));
    }

    /**
     * 유저 엔티티 조회(List)
     */
    public List<User> getUserEntityList(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    /**
     * 중복 이메일 체크
     */
    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException("Duplicate Email : " + email);
        }
    }

    /**
     * 조직 정보 할당
     */
    private void assignOrgIfPresent(String teamName, String positionName, String gradeName, User user) {
        if (teamName != null) assignTeam(teamName, user);

        if (positionName != null) assignPosition(positionName, user);

        if (gradeName != null) assignGrade(gradeName, user);
    }

    /**
     * 부서 할당
     */
    private void assignTeam(String teamName, User user) {
        Team team = teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new NotFoundException("Not Found Team : " + teamName));
        user.assignTeam(team);
    }

    /**
     * 직책 할당
     */
    private void assignPosition(String positionName, User user) {
        Position position = positionRepository.findByPositionName(positionName)
                .orElseThrow(() -> new NotFoundException("Not Found Position : " + positionName));
        user.assignPosition(position);
    }

    /**
     * 직급 할당
     */
    private void assignGrade(String gradeName, User user) {
        Grade grade = gradeRepository.findByGradeName(gradeName)
                .orElseThrow(() -> new NotFoundException("Not Found Grade : " + gradeName));
        user.assignGrade(grade);
    }
}
