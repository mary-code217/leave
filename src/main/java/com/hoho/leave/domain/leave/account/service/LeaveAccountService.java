package com.hoho.leave.domain.leave.account.service;

import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.domain.leave.account.service.support.AccrualSchedule;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import com.hoho.leave.domain.leave.account.repository.UserLeavesRepository;
import com.hoho.leave.domain.leave.account.service.support.AccrualPolicyEngine;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LeaveAccountService {

    private final UserLeavesRepository userLeavesRepository;
    private final AccrualPolicyEngine engine;

    public UserLeaves firstCreateUserLeaves(User user, BigDecimal balanceDays) {
        checkDuplicateUserLeaves(user);
        AccrualSchedule accrualSchedule = engine.getAccountEvent(user);
        UserLeaves userLeaves = UserLeaves.create(user, accrualSchedule, balanceDays);
        return userLeavesRepository.save(userLeaves);
    }

    /**
     * 중복된 UserLeaves 가 있는지 확인
     */
    private void checkDuplicateUserLeaves(User user) {
        if (userLeavesRepository.existsByUserId(user.getId())) {
            throw new DuplicateException("Duplicate UserLeaves for userId: " + user.getId());
        }
    }
}
