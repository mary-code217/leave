package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.domain.leave.policy.dto.request.UserDefaultApproverCreate;
import com.hoho.leave.domain.leave.policy.entity.UserDefaultApprover;
import com.hoho.leave.domain.leave.policy.repository.UserDefaultApproverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 기본 결재자 서비스.
 * 
 * 사용자별 기본 결재자 설정 기능을 제공한다.
 * 
 */
@Service
@RequiredArgsConstructor
public class UserDefaultApproverService {

    private final UserDefaultApproverRepository defaultApproverRepository;

    /**
     * 사용자 기본 결재자를 생성한다.
     *
     * @param request 기본 결재자 생성 요청
     * @return 생성된 기본 결재자 엔티티
     */
    @Transactional
    public UserDefaultApprover createUserDefaultApprover(UserDefaultApproverCreate request) {




        return null;
    }
}
