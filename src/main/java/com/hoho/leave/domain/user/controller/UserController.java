package com.hoho.leave.domain.user.controller;

import com.hoho.leave.domain.user.dto.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserJoinRequest userJoinRequest) {

        System.out.println(userJoinRequest.getUsername());
        System.out.println(userJoinRequest.getEmail());
        System.out.println(userJoinRequest.getHireDate());

        return  ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

}
