package com.hoho.leave.util.exception.config;

import com.hoho.leave.util.exception.BusinessException;
import com.hoho.leave.util.exception.DuplicateException;
import com.hoho.leave.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandleConfig {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException e) {
        return getProblemDetail(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(DuplicateException.class)
    public ProblemDetail handleDuplicateEmail(DuplicateException e) {
        return getProblemDetail(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(NotFoundException.class)
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


