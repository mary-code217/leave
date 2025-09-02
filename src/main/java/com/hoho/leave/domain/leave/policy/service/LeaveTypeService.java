package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.domain.leave.policy.dto.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.policy.repository.LeaveTypeRepository;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public void LeaveTypeCreate(LeaveTypeCreateRequest leaveTypeCreateRequest) {
        if(leaveTypeRepository.existsByLeaveName(leaveTypeCreateRequest.getLeaveTypeName())) {
            throw new BusinessException("생성 실패 - 이미 존재하는 유형 입니다.");
        }

        leaveTypeRepository.save(LeaveType.Create(leaveTypeCreateRequest));
    }

    @Transactional
    public void LeaveTypeDelete(Long leaveTypeId) {
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new BusinessException("삭제 실패 - 존재하지 않는 유형 입니다."));

        leaveTypeRepository.delete(leaveType);
    }
}
