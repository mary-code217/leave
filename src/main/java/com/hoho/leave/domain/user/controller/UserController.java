package com.hoho.leave.domain.user.controller;

import com.hoho.leave.domain.user.dto.request.UserJoinRequest;
import com.hoho.leave.domain.user.dto.request.UserUpdateRequest;
import com.hoho.leave.domain.user.dto.response.UserDetailResponse;
import com.hoho.leave.domain.user.dto.response.UserListResponse;
import com.hoho.leave.domain.user.facade.UserFacade;
import com.hoho.leave.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관리 컨트롤러.
 * <p>
 * 사용자 계정의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * </p>
 */
@Tag(name = "User API", description = "유저 도메인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserFacade userApplicationService;

    /**
     * 새로운 사용자를 생성한다.
     *
     * @param userJoinRequest 회원가입 요청 정보
     * @return 회원가입 성공 메시지
     */
    @Operation(summary = "회원가입 API")
    @PostMapping("/join")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserJoinRequest userJoinRequest) {

        userApplicationService.createUser(userJoinRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    /**
     * 사용자 상세 정보를 조회한다.
     *
     * @param userId 조회할 사용자 ID
     * @return 사용자 상세 정보
     */
    @Operation(summary = "유저 상세 조회 API")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResponse> getUser(@PathVariable Long userId) {

        UserDetailResponse response = userService.getUser(userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자 정보를 수정한다.
     *
     * @param userId 수정할 사용자 ID
     * @param userUpdateRequest 수정할 사용자 정보
     * @return 수정 성공 메시지
     */
    @Operation(summary = "유저 정보 변경 API")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateUser(userId, userUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("유저 변경 성공");
    }

    /**
     * 사용자 목록을 페이지 단위로 조회한다.
     *
     * @param size 페이지당 항목 수 (기본값: 10, 최소: 1, 최대: 20)
     * @param page 페이지 번호 (기본값: 1, 최소: 1)
     * @return 사용자 목록 응답
     */
    @Operation(summary = "유저 목록 조회 API")
    @GetMapping("")
    public ResponseEntity<UserListResponse> getAllUsers(
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size,
            @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        UserListResponse response = userService.getAllUsers(size, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자를 삭제한다.
     *
     * @param userId 삭제할 사용자 ID
     * @return 삭제 성공 메시지
     */
    @Operation(summary = "유저 삭제 API")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body("유저 삭제 성공");
    }
}
