package com.hoho.leave.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * 애플리케이션 전역 예외 처리 핸들러.
 * 
 * 모든 컨트롤러에서 발생하는 예외를 가로채어 RFC 7807 표준의
 * ProblemDetail 형식으로 일관된 오류 응답을 생성한다.
 * 
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 처리되지 않은 모든 예외를 처리한다.
     *
     * @param e 발생한 예외
     * @return 500 Internal Server Error 상태의 ProblemDetail
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        log.error("Unhandled exception occurred", e);
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    /**
     * 비즈니스 로직 예외 및 파일 오류 예외를 처리한다.
     *
     * @param e 발생한 비즈니스 예외
     * @return 400 Bad Request 상태의 ProblemDetail
     */
    @ExceptionHandler({BusinessException.class, FileErrorException.class})
    public ProblemDetail handleBusiness(BusinessException e) {
        log.warn("Business exception: {}", e.getMessage());
        return getProblemDetail(HttpStatus.BAD_REQUEST, e);
    }

    /**
     * 중복 데이터 예외를 처리한다.
     *
     * @param e 발생한 중복 예외
     * @return 409 Conflict 상태의 ProblemDetail
     */
    @ExceptionHandler(DuplicateException.class)
    public ProblemDetail handleDuplicateEmail(DuplicateException e) {
        log.warn("Duplicate exception: {}", e.getMessage());
        return getProblemDetail(HttpStatus.CONFLICT, e);
    }

    /**
     * 리소스를 찾을 수 없는 예외를 처리한다.
     *
     * @param e 발생한 NotFoundException
     * @return 404 Not Found 상태의 ProblemDetail
     */
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException e) {
        log.info("Resource not found: {}", e.getMessage());
        return getProblemDetail(HttpStatus.NOT_FOUND, e);
    }

    /**
     * ProblemDetail 객체를 생성한다.
     *
     * @param status HTTP 상태 코드
     * @param e      발생한 예외
     * @return 타임스탬프와 예외 정보가 포함된 ProblemDetail
     */
    private static ProblemDetail getProblemDetail(HttpStatus status, Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, e.getMessage());

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", e.getClass().getSimpleName());

        return problemDetail;
    }
}


