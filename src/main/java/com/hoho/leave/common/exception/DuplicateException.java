package com.hoho.leave.common.exception;

/**
 * 중복 데이터 발생 시 던져지는 예외 클래스.
 * <p>
 * 이메일, 사용자명 등 고유해야 하는 값이 이미 존재할 때 사용된다.
 * </p>
 */
public class DuplicateException extends RuntimeException {

    /**
     * 지정된 메시지로 DuplicateException을 생성한다.
     *
     * @param message 예외 상세 메시지
     */
    public DuplicateException(String message) {
        super(message);
    }
}
