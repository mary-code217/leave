package com.hoho.leave.common.exception;

/**
 * 요청한 리소스를 찾을 수 없을 때 던져지는 예외 클래스.
 * 
 * 데이터베이스에서 엔티티를 찾을 수 없거나, 존재하지 않는 리소스에 접근할 때 사용된다.
 * 
 */
public class NotFoundException extends RuntimeException {

    /**
     * 지정된 메시지로 NotFoundException을 생성한다.
     *
     * @param message 예외 상세 메시지
     */
    public NotFoundException(String message) {
        super(message);
    }
}
