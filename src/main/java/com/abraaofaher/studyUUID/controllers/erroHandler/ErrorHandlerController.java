package com.abraaofaher.studyUUID.controllers.erroHandler;

import com.abraaofaher.studyUUID.model.exceptions.InvalidIdentify;
import com.abraaofaher.studyUUID.model.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<ErrorResponseBody> handleNotFoundExceptionError(NotFoundException notFoundException, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseBody(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not found in db",
                notFoundException.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(value = {InvalidIdentify.class})
    protected ResponseEntity<ErrorResponseBody> handleNotFoundExceptionError(InvalidIdentify invalidIdentify, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseBody(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Identify is invalid",
                invalidIdentify.getMessage(),
                request.getRequestURI()));
    }
}