package com.hoho.leave.common.exception;

/**
 * 파일 처리 중 발생하는 예외를 나타내는 클래스.
 * <p>
 * 파일 업로드, 다운로드, 삭제 등 파일 관련 작업 실패 시 사용된다.
 * </p>
 */
public class FileErrorException extends RuntimeException {

    /**
     * 지정된 메시지로 FileErrorException을 생성한다.
     *
     * @param message 예외 상세 메시지
     */
    public FileErrorException(String message) {
        super(message);
    }
}
