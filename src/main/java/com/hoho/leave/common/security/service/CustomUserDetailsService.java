package com.hoho.leave.common.security.service;

import com.hoho.leave.common.security.principal.CustomUserDetails;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 UserDetailsService 구현체.
 * <p>
 * 데이터베이스에서 사용자 정보를 조회하여 인증에 필요한 UserDetails 객체를 제공한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 이메일로 사용자 정보를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 사용자 상세 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailsService.loadUserByUsername 호출");

        String key = normalize(email);

        User user = userRepository.findByEmail(key)
                .orElseThrow(() -> new UsernameNotFoundException("조회 실패 - 존재하지 않는 이메일 입니다."));

        return new CustomUserDetails(user);
    }

    /**
     * 이메일을 정규화한다.
     * <p>
     * 공백을 제거하고 소문자로 변환한다.
     * </p>
     *
     * @param email 원본 이메일
     * @return 정규화된 이메일
     */
    private String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
