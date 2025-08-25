package com.hoho.leave.domain.user.service;

import com.hoho.leave.domain.user.dto.UserJoinRequest;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    void createUser(UserJoinRequest userJoinRequest) {

    }
}
