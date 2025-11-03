package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.policy.repository.LeaveTypeRepository;
import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestCreateRequest;
import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestUpdateRequest;
import com.hoho.leave.domain.leave.request.dto.response.LeaveRequestDetailResponse;
import com.hoho.leave.domain.leave.request.dto.response.LeaveRequestListResponse;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.repository.LeaveRequestRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public void createLeaveRequest(LeaveRequestCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("신청 실패 - 존재하지 않는 유저 입니다."));

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new NotFoundException("신청 실패 - 존재하지 않는 유형 입니다."));

        LeaveRequest leaveRequest = LeaveRequest.create(request);
        leaveRequest.addUser(user);
        leaveRequest.addLeaveType(leaveType);

        leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public void updateLeaveRequest(Long leaveRequestId, LeaveRequestUpdateRequest request) {
        LeaveRequest leaveRequest = getRequestEntity(leaveRequestId);

        leaveRequest.updateStatus(request.getStatus());
    }

    @Transactional
    public void deleteLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = getRequestEntity(leaveRequestId);

        leaveRequestRepository.delete(leaveRequest);
    }

    @Transactional(readOnly = true)
    public LeaveRequestDetailResponse getLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = getRequestEntity(leaveRequestId);

        return LeaveRequestDetailResponse.of(leaveRequest);
    }

    @Transactional(readOnly = true)
    public LeaveRequestListResponse getUserLeaveRequests(Long userId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);

        Page<LeaveRequest> pageList = leaveRequestRepository.findByUserId(userId, pageable);

        List<LeaveRequestDetailResponse> list = pageList.stream()
                .map(LeaveRequestDetailResponse::of)
                .toList();

        return LeaveRequestListResponse.of(pageList, list);
    }

    @Transactional(readOnly = true)
    public LeaveRequestListResponse getAllLeaveRequests(Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);

        Page<LeaveRequest> pageList = leaveRequestRepository.findAll(pageable);

        List<LeaveRequestDetailResponse> list = pageList.stream()
                .map(LeaveRequestDetailResponse::of)
                .toList();

        return LeaveRequestListResponse.of(pageList, list);
    }

    /** 휴가 신청서 엔티티 조회 */
    private LeaveRequest getRequestEntity(Long leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new NotFoundException("Not Found LeaveRequest : " + leaveRequestId));
    }

    /** 페이지 요청 정보 */
    private static PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("startDay")));
    }
}
