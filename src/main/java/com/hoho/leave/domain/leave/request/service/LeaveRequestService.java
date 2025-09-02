package com.hoho.leave.domain.leave.request.service;

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
import com.hoho.leave.util.exception.BusinessException;
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
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public void createLeaveRequest(LeaveRequestCreateRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("신청 실패 - 존재하지 않는 유저 입니다."));

        LeaveType leaveType = leaveTypeRepository.findById(req.getLeaveTypeId())
                .orElseThrow(() -> new BusinessException("신청 실패 - 존재하지 않는 유형 입니다."));

        LeaveRequest leaveRequest = LeaveRequest.create(req);
        leaveRequest.addUser(user);
        leaveRequest.addLeaveType(leaveType);

        leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public void updateLeaveRequest(Long leaveRequestId, LeaveRequestUpdateRequest req) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new BusinessException("변경 실패 - 존재하지 않는 신청 입니다."));

        leaveRequest.updateStatus(req.getStatus());
    }

    @Transactional
    public void deleteLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new BusinessException("취소 실패 - 존재하지 않는 신청 입니다."));

        leaveRequestRepository.delete(leaveRequest);
    }

    @Transactional(readOnly = true)
    public LeaveRequestDetailResponse getLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new BusinessException("조회 실패 - 존재하지 않는 신청 입니다."));

        LeaveRequestDetailResponse leaveRequestDetailResponse = LeaveRequestDetailResponse.from(leaveRequest);
        leaveRequestDetailResponse.addUser(leaveRequest.getUser());
        leaveRequestDetailResponse.addLeaveType(leaveRequest.getLeaveType());

        return leaveRequestDetailResponse;
    }

    @Transactional(readOnly = true)
    public LeaveRequestListResponse getUserLeaveRequests(Long userId, Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Order.asc("startDay"));
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<LeaveRequest> requests = leaveRequestRepository.findByUserId(userId, pageable);

        List<LeaveRequestDetailResponse> list = new ArrayList<>();
        for(LeaveRequest l : requests.toList()) {
            LeaveRequestDetailResponse detail = LeaveRequestDetailResponse.from(l);
            detail.addUser(l.getUser());
            detail.addLeaveType(l.getLeaveType());
            list.add(detail);
        }

        return LeaveRequestListResponse.from(
                page,
                size,
                list,
                requests.getTotalPages(),
                requests.getTotalElements(),
                requests.isFirst(),
                requests.isLast()
        );
    }

    @Transactional(readOnly = true)
    public LeaveRequestListResponse getAllLeaveRequests(Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Order.asc("startDay"));
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<LeaveRequest> requests = leaveRequestRepository.findAll(pageable);

        List<LeaveRequestDetailResponse> list = new ArrayList<>();
        for(LeaveRequest l : requests.toList()) {
            LeaveRequestDetailResponse detail = LeaveRequestDetailResponse.from(l);
            detail.addUser(l.getUser());
            detail.addLeaveType(l.getLeaveType());
            list.add(detail);
        }

        return LeaveRequestListResponse.from(
                page,
                size,
                list,
                requests.getTotalPages(),
                requests.getTotalElements(),
                requests.isFirst(),
                requests.isLast()
        );
    }
}
