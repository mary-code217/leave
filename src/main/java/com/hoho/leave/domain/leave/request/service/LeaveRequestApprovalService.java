package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.domain.leave.request.repository.LeaveRequestApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveRequestApprovalService {

    private final LeaveRequestApprovalRepository leaveRequestApprovalRepository;

    public void createApproval() {

    }

}
