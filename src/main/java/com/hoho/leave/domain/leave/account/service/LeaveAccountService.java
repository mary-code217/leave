package com.hoho.leave.domain.leave.account.service;

import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.leave.account.dto.response.UserLeavesDetailResponse;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import com.hoho.leave.domain.leave.account.repository.UserLeavesRepository;
import com.hoho.leave.domain.leave.account.service.support.AccrualPolicyEngine;
import com.hoho.leave.domain.leave.account.service.support.AccrualSchedule;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 사용자 휴가 계정 서비스.
 * 
 * 사용자의 휴가 계정 생성, 조회 기능을 제공한다.
 * 
 */
@Service
@RequiredArgsConstructor
public class LeaveAccountService {

    private final UserLeavesRepository userLeavesRepository;
    private final AccrualPolicyEngine engine;

    /**
     * 사용자의 첫 휴가 계정을 생성한다.
     *
     * @param user 사용자
     * @param balanceDays 초기 잔여 일수
     * @return 생성된 휴가 계정
     */
    public UserLeaves firstCreateUserLeaves(User user, BigDecimal balanceDays) {
        checkDuplicateUserLeaves(user);
        AccrualSchedule accrualSchedule = engine.getAccountEvent(user);
        UserLeaves userLeaves = UserLeaves.create(user, accrualSchedule, balanceDays);
        return userLeavesRepository.save(userLeaves);
    }

    /**
     * 사용자의 휴가 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 휴가 정보 응답
     */
    @Transactional(readOnly = true)
    public UserLeavesDetailResponse getUserLeaves(Long userId) {
        UserLeaves userLeaves = getUserLeavesByUserId(userId);
        return UserLeavesDetailResponse.of(userLeaves);
    }

    /**
     * 사용자 ID로 휴가 계정 엔티티를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 휴가 계정 엔티티
     */
    public UserLeaves getUserLeavesByUserId(Long userId) {
        return userLeavesRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("UserLeaves not found for userId: " + userId));
    }

    /**
     * 중복된 휴가 계정이 있는지 확인한다.
     *
     * @param user 사용자
     */
    private void checkDuplicateUserLeaves(User user) {
        if (userLeavesRepository.existsByUserId(user.getId())) {
            throw new DuplicateException("Duplicate UserLeaves for userId: " + user.getId());
        }
    }
}
