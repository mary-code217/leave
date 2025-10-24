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
import com.hoho.leave.util.exception.BusinessException;
import com.hoho.leave.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PositionRepository positionRepository;
    private final GradeRepository gradeRepository;

    private final PasswordEncoder passwordEncoder;

    public User createUser(UserJoinRequest userJoinRequest) {
        checkDuplicateEmail(userJoinRequest.getEmail());

        User user = User.create(userJoinRequest, passwordEncoder);
        if (userJoinRequest.getTeamId() != null) {
            Team team = teamRepository.findById(userJoinRequest.getTeamId())
                    .orElseThrow(() -> new BusinessException("회원 가입 실패 - 존재하지 않는 부서 입니다."));
            user.assignTeam(team);
        }
        if (userJoinRequest.getPositionId() != null) {
            Position position = positionRepository.findById(userJoinRequest.getPositionId())
                    .orElseThrow(() -> new BusinessException("회원 가입 실패 - 존재하지 않는 직책 입니다."));
            user.assignPosition(position);
        }
        if (userJoinRequest.getGradeId() != null) {
            Grade grade = gradeRepository.findById(userJoinRequest.getGradeId())
                    .orElseThrow(() -> new BusinessException("회원 가입 실패 - 존재하지 않는 직급 입니다."));
            user.assignGrade(grade);
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUser(Long userId) {
        User findUser = getUserEntity(userId);

        return UserDetailResponse.of(findUser);
    }

    @Transactional(readOnly = true)
    public UserListResponse getAllUsers(Integer size, Integer page) {
        Sort sort = Sort.by(Sort.Order.asc("hireDate"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<User> users = userRepository.findPageWithOrg(pageable);
        List<UserDetailResponse> list = new ArrayList<>();

        for (User u : users.getContent()) {
            list.add(UserDetailResponse.of(u));
        }

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
    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User findUser = getUserEntity(userId);

        Team team = teamRepository.findByTeamName(userUpdateRequest.getTeamName())
                .orElseThrow(() -> new BusinessException("변경 실패 - 존재하지 않는 부서 입니다."));
        Position position = positionRepository.findByPositionName(userUpdateRequest.getPositionName())
                .orElseThrow(() -> new BusinessException("변경 실패 - 존재하지 않는 직책 입니다."));
        Grade grade = gradeRepository.findByGradeName(userUpdateRequest.getGradeName())
                .orElseThrow(() -> new BusinessException("변경 실패 - 존재하지 않는 직급 입니다."));

        findUser.updatePassword(userUpdateRequest.getPassword(), passwordEncoder);
        findUser.updateUsername(userUpdateRequest.getUsername());
        findUser.assignGrade(grade);
        findUser.assignPosition(position);
        findUser.assignTeam(team);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserEntity(userId);

        userRepository.delete(user);
    }

    private User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not Found User: " + userId));
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Duplicate Email: " + email);
        }
    }
}
