package com.hoho.leave.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 관련 설정 클래스.
 * <p>
 * JPA Auditing 기능을 활성화하여 엔티티의 생성일시, 수정일시 등을 자동으로 관리한다.
 * </p>
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {

}