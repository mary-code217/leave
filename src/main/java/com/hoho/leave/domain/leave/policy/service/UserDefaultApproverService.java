package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.domain.leave.policy.dto.request.UserDefaultApproverCreate;
import com.hoho.leave.domain.leave.policy.entity.UserDefaultApprover;
import com.hoho.leave.domain.leave.policy.repository.UserDefaultApproverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDefaultApproverService {

    private final UserDefaultApproverRepository defaultApproverRepository;

    @Transactional
    public UserDefaultApprover createUserDefaultApprover(UserDefaultApproverCreate request) {




        return null;
    }
}
