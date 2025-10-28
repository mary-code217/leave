package com.hoho.leave.domain.leave.account.service;

import com.hoho.leave.domain.leave.account.dto.AccountEvent;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import com.hoho.leave.domain.leave.account.repository.UserLeavesRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LeaveAccountService {

    private final UserLeavesRepository userLeavesRepository;

    private final AccrualPolicyEngine engine;

    public UserLeaves firstCreateUserLeaves(User user, BigDecimal balanceDays) {
        if (userLeavesRepository.existsByUserId(user.getId())) {
            throw new BusinessException("생성 실패 - 휴가 계정이 존재하는 유저 입니다.");
        }

        AccountEvent accountEvent = engine.getAccountEvent(user);
        UserLeaves leaves = UserLeaves.create(user, accountEvent.getLeaveStage(), accountEvent.getNextAccrualAt(), balanceDays);
        return userLeavesRepository.save(leaves);
    }
}
