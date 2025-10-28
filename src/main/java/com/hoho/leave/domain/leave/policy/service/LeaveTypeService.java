package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeUpdateRequest;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeDetailResponse;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeListResponse;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.policy.repository.LeaveTypeRepository;
import com.hoho.leave.common.exception.BusinessException;
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
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public void LeaveTypeCreate(LeaveTypeCreateRequest leaveTypeCreateRequest) {
        if (leaveTypeRepository.existsByLeaveName(leaveTypeCreateRequest.getLeaveTypeName())) {
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

    @Transactional
    public void LeaveTypeUpdate(Long leaveTypeId, LeaveTypeUpdateRequest leaveTypeUpdate) {
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new BusinessException("변경 실패 - 존재하지 않는 유형 입니다."));

        leaveType.Update(leaveTypeUpdate);
    }

    @Transactional(readOnly = true)
    public LeaveTypeDetailResponse getLeaveType(Long leaveTypeId) {
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new BusinessException("조회 실패 - 존재하지 않는 유형 입니다."));

        return LeaveTypeDetailResponse.from(leaveType);
    }

    @Transactional(readOnly = true)
    public LeaveTypeListResponse getAllLeaveTypes(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Order.desc("unitDays"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<LeaveType> leaveTypes = leaveTypeRepository.findAll(pageable);

        List<LeaveTypeDetailResponse> list = leaveTypes.getContent().stream().map(LeaveTypeDetailResponse::from).toList();

        return LeaveTypeListResponse.of(
                page,
                size,
                list,
                leaveTypes.getTotalPages(),
                leaveTypes.getTotalElements(),
                leaveTypes.isFirst(),
                leaveTypes.isLast()
        );
    }

    public LeaveType getLeaveTypeEntity(Long leaveTypeId) {
        return leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new BusinessException("조회 실패 - 존재하지 않는 유형 입니다."));
    }
}
