package com.hoho.leave.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException e) {
        return getProblemDetail(HttpStatus.BAD_REQUEST, e);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateException.class)
    public ProblemDetail handleDuplicateEmail(DuplicateException e) {
        return getProblemDetail(HttpStatus.CONFLICT, e);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException e) {
        return getProblemDetail(HttpStatus.NOT_FOUND, e);
    }

    private static ProblemDetail getProblemDetail(HttpStatus status, Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, e.getMessage());

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", e.getClass().getSimpleName());

        return problemDetail;
    }
}


