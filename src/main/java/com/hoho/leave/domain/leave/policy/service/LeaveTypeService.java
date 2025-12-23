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

/**
 * 휴가 유형 서비스.
 * 
 * 휴가 유형의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * 
 */
@Service
@RequiredArgsConstructor
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    /**
     * 휴가 유형을 생성한다.
     *
     * @param request 휴가 유형 생성 요청
     */
    @Transactional
    public void createLeaveType(LeaveTypeCreateRequest request) {
        checkDuplicateLeaveType(request);
        leaveTypeRepository.save(LeaveType.create(request));
    }

    /**
     * 휴가 유형을 삭제한다.
     *
     * @param leaveTypeId 휴가 유형 ID
     */
    @Transactional
    public void deleteLeaveType(Long leaveTypeId) {
        LeaveType leaveType = getLeaveTypeEntity(leaveTypeId);
        leaveTypeRepository.delete(leaveType);
    }

    /**
     * 휴가 유형을 수정한다.
     *
     * @param leaveTypeId 휴가 유형 ID
     * @param request 휴가 유형 수정 요청
     */
    @Transactional
    public void updateLeaveType(Long leaveTypeId, LeaveTypeUpdateRequest request) {
        LeaveType leaveType = getLeaveTypeEntity(leaveTypeId);
        leaveType.update(request);
    }

    /**
     * 휴가 유형 상세 정보를 조회한다.
     *
     * @param leaveTypeId 휴가 유형 ID
     * @return 휴가 유형 상세 응답
     */
    @Transactional(readOnly = true)
    public LeaveTypeDetailResponse getLeaveType(Long leaveTypeId) {
        LeaveType leaveType = getLeaveTypeEntity(leaveTypeId);

        return LeaveTypeDetailResponse.of(leaveType);
    }

    /**
     * 휴가 유형 목록을 페이징하여 조회한다.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 휴가 유형 목록 응답
     */
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
     * 페이지 정보를 생성한다.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이지 요청 정보
     */
    private static PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("unitDays")));
    }

    /**
     * 휴가 유형 엔티티를 조회한다.
     *
     * @param leaveTypeId 휴가 유형 ID
     * @return 휴가 유형 엔티티
     */
    public LeaveType getLeaveTypeEntity(Long leaveTypeId) {
        return leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new NotFoundException("Not Found Leave Type : " + leaveTypeId));
    }

    /**
     * 휴가 유형 중복 여부를 확인한다.
     *
     * @param request 휴가 유형 생성 요청
     */
    private void checkDuplicateLeaveType(LeaveTypeCreateRequest request) {
        if (leaveTypeRepository.existsByLeaveName(request.getLeaveTypeName())) {
            throw new DuplicateException("Duplicate Leave Type Name exists : " + request.getLeaveTypeName());
        }
    }
}
