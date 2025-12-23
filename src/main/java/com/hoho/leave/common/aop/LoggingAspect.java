package com.hoho.leave.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 메서드 실행 시간을 로깅하는 AOP Aspect.
 * 
 * domain 패키지 하위의 모든 메서드 실행 시간을 측정하여 로그로 기록한다.
 * 
 */
@Aspect
@Slf4j
@Component
public class LoggingAspect {

    /**
     * 메서드 실행 시간을 측정하고 로깅한다.
     * 
     * com.hoho.leave.domain 패키지 하위의 모든 메서드에 적용된다.
     * 
     *
     * @param joinPoint AOP 조인 포인트
     * @return 원본 메서드의 반환값
     * @throws Throwable 메서드 실행 중 발생한 예외
     */
    @Around("execution(* com.hoho.leave.domain..*.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.info("Method {} executed in {} ms", methodName, duration);
        }
    }
}
