package com.hoho.leave.config.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 스케줄링 관련 설정 클래스.
 * 
 * Spring의 스케줄링 기능을 활성화하여 정기적인 작업 실행을 가능하게 한다.
 * 공휴일 동기화, 휴가 적립 등의 배치 작업에 사용된다.
 * 
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
