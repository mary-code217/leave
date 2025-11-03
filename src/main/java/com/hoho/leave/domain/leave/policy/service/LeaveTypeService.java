package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeUpdateRequest;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeDetailResponse;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeListResponse;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.policy.repository.LeaveTypeRepository;
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
    public void LeaveTypeCreate(LeaveTypeCreateRequest request) {
        checkDuplicateLeaveType(request);
        leaveTypeRepository.save(LeaveType.Create(request));
    }

    @Transactional
    public void LeaveTypeDelete(Long leaveTypeId) {
        LeaveType leaveType = getLeaveTypeEntity(leaveTypeId);
        leaveTypeRepository.delete(leaveType);
    }

    @Transactional
    public void LeaveTypeUpdate(Long leaveTypeId, LeaveTypeUpdateRequest request) {
        LeaveType leaveType = getLeaveTypeEntity(leaveTypeId);
        leaveType.Update(request);
    }

    @Transactional(readOnly = true)
    public LeaveTypeDetailResponse getLeaveType(Long leaveTypeId) {
        LeaveType leaveType = getLeaveTypeEntity(leaveTypeId);

        return LeaveTypeDetailResponse.of(leaveType);
    }

    @Transactional(readOnly = true)
    public LeaveTypeListResponse getAllLeaveTypes(Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<LeaveType> pageList = leaveTypeRepository.findAll(pageable);
        List<LeaveTypeDetailResponse> list = pageList.getContent()
                .stream()
                .map(LeaveTypeDetailResponse::of)
                .toList();

        return LeaveTypeListResponse.of(pageList, list);
    }

    /**
     * 페이지 정보 생성
     */
    private static PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("unitDays")));
    }

    /**
     * 휴가 유형 엔티티 조회
     */
    public LeaveType getLeaveTypeEntity(Long leaveTypeId) {
        return leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new NotFoundException("Not Found Leave Type : " + leaveTypeId));
    }

    /**
     * 휴가 유형 중복 체크
     */
    private void checkDuplicateLeaveType(LeaveTypeCreateRequest request) {
        if (leaveTypeRepository.existsByLeaveName(request.getLeaveTypeName())) {
            throw new DuplicateException("Duplicate Leave Type Name exists : " + request.getLeaveTypeName());
        }
    }
}
