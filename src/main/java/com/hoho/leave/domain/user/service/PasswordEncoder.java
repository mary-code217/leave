package com.hoho.leave.domain.user.service;

public interface PasswordEncoder {
    String encode(String password);
    boolean matches(String password, String passwordHash);
}
