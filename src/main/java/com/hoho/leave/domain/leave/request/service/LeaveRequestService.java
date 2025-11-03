package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.policy.repository.LeaveTypeRepository;
import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestCreateRequest;
import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestUpdateRequest;
import com.hoho.leave.domain.leave.request.dto.response.LeaveRequestDetailResponse;
import com.hoho.leave.domain.leave.request.dto.response.LeaveRequestListResponse;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.repository.AttachmentRepository;
import com.hoho.leave.domain.leave.request.repository.AttachmentRepository.ReqIdCount;
import com.hoho.leave.domain.leave.request.repository.LeaveRequestRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public void createLeaveRequest(LeaveRequestCreateRequest request) {
        User user = getUser(request);
        LeaveType leaveType = getLeaveType(request);

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

    public void deleteLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.delete(leaveRequest);
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

        List<Long> ids = pageList.stream().map(LeaveRequest::getId).toList();
        Map<Long, Long> countMap = attachmentRepository.countByLeaveRequestIds(ids).stream()
                .collect(Collectors.toMap(ReqIdCount::getReqId, ReqIdCount::getCnt));

        List<LeaveRequestDetailResponse> list = pageList
                .map(req -> getDetailResponse(req, countMap)).toList();

        return LeaveRequestListResponse.of(pageList, list);
    }

    /** 휴가 신청서 정보 응답 DTO 생성 */
    private LeaveRequestDetailResponse getDetailResponse(LeaveRequest req, Map<Long, Long> countMap) {
        LeaveRequestDetailResponse response = LeaveRequestDetailResponse.of(req);
        response.hasAttachment(countMap.getOrDefault(req.getId(), 0L) > 0);
        return response;
    }

    /** 휴가 신청서 엔티티 조회 */
    public LeaveRequest getRequestEntity(Long leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new NotFoundException("Not Found LeaveRequest : " + leaveRequestId));
    }

    /** 휴가 신청서 엔티티 조회(유저, 타입 포함) */
    public LeaveRequest getRequestEntityWithUserAndType(Long leaveRequestId) {
        return leaveRequestRepository.findByIdWithUserAndLeaveType(leaveRequestId)
                .orElseThrow(() -> new NotFoundException("Not Found LeaveRequest : " + leaveRequestId));
    }

    /** 페이지 요청 정보 */
    private static PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("startDay")));
    }

    /** 유저 엔티티 조회 */
    private User getUser(LeaveRequestCreateRequest request) {
        return userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Not Found User : " + request.getUserId()));
    }

    /** 휴가 유형 엔티티 조회 */
    private LeaveType getLeaveType(LeaveRequestCreateRequest request) {
        return leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new NotFoundException("Not Found Leave Type : " + request.getLeaveTypeId()));
    }
}
