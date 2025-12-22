package com.hoho.leave.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 컨트롤러.
 * <p>
 * 관리자 페이지 접근 관련 기능을 제공한다.
 * </p>
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     * 관리자 페이지를 반환한다.
     *
     * @return 관리자 페이지 메시지
     */
    @GetMapping("")
    public String adminPage() {
        return "hello admin";
    }
}
