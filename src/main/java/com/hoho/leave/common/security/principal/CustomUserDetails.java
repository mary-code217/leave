package com.hoho.leave.common.security.principal;

import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Security의 UserDetails 구현체.
 * 
 * User 엔티티를 래핑하여 인증 및 권한 부여에 필요한 정보를 제공한다.
 * 
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * 사용자의 권한 목록을 반환한다.
     *
     * @return 권한 컬렉션
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });

        return authorities;
    }

    /**
     * 사용자 ID를 반환한다.
     *
     * @return 사용자 ID
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * 사용자 비밀번호를 반환한다.
     *
     * @return 암호화된 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 인증에 사용되는 사용자명(이메일)을 반환한다.
     *
     * @return 사용자 이메일
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 화면에 표시할 사용자 이름을 반환한다.
     *
     * @return 사용자 실명
     */
    public String getDisplayName() {
        return user.getUsername();
    }

    /**
     * 사원번호를 반환한다.
     *
     * @return 사원번호
     */
    public String getEmployeeNo() {
        return user.getEmployeeNo();
    }

    /**
     * 입사일을 반환한다.
     *
     * @return 입사일
     */
    public LocalDate getHireDate() {
        return user.getHireDate();
    }

    /**
     * 소속 팀을 반환한다.
     *
     * @return 팀 정보
     */
    public Team getTeam() {
        return user.getTeam();
    }

    /**
     * 직급을 반환한다.
     *
     * @return 직급 정보
     */
    public Grade getGrade() {
        return user.getGrade();
    }

    /**
     * 직책을 반환한다.
     *
     * @return 직책 정보
     */
    public Position getPosition() {
        return user.getPosition();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * 계정 활성화 여부를 반환한다.
     *
     * @return 활성화되었으면 true, 비활성화되었으면 false
     */
    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
