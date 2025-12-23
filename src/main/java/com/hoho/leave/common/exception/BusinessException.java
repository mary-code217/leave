package com.hoho.leave.common.exception;

/**
 * 비즈니스 로직 처리 중 발생하는 예외를 나타내는 클래스.
 * 
 * 비즈니스 규칙 위반, 유효하지 않은 상태 전이 등 업무 로직상의 오류 상황에서 사용된다.
 * 
 */
public class BusinessException extends RuntimeException {

    /**
     * 지정된 메시지로 BusinessException을 생성한다.
     *
     * @param message 예외 상세 메시지
     */
    public BusinessException(String message) {
        super(message);
    }
}
