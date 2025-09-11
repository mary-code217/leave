package com.hoho.leave.domain.leave.account.service;

import com.hoho.leave.domain.leave.account.entity.LeaveStage;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import com.hoho.leave.domain.leave.account.repository.UserLeavesRepository;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveAccountService {

    private final UserLeavesRepository userLeavesRepository;

    private final AccrualPolicyEngine engine;

    public void createUserLeaves(User user, LeaveType leaveType, LeaveStage stage, LocalDate nextAccrualAt) {
        if(userLeavesRepository.existsByUserIdAndLeaveTypeId(user.getId(), leaveType.getId())) {
           throw new BusinessException("생성 실패 - 이미 존재하는 휴가 계정 입니다.");
        }

        userLeavesRepository.save(UserLeaves.create(user, leaveType, stage, nextAccrualAt));
    }
}
