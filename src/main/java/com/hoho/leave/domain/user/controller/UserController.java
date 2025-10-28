package com.hoho.leave.domain.user.controller;

import com.hoho.leave.domain.user.dto.request.UserJoinRequest;
import com.hoho.leave.domain.user.dto.request.UserUpdateRequest;
import com.hoho.leave.domain.user.dto.response.UserDetailResponse;
import com.hoho.leave.domain.user.dto.response.UserListResponse;
import com.hoho.leave.domain.user.facade.UserFacade;
import com.hoho.leave.domain.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserFacade userApplicationService;

    @PostMapping("/join")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserJoinRequest userJoinRequest) {

        userApplicationService.createUser(userJoinRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResponse> getUser(@PathVariable Long userId) {

        UserDetailResponse response = userService.getUser(userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateUser(userId, userUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("유저 변경 성공");
    }

    @GetMapping("")
    public ResponseEntity<UserListResponse> getAllUsers(
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size,
            @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        UserListResponse response = userService.getAllUsers(size, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body("유저 삭제 성공");
    }
}
