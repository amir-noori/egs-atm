package com.egs.ATMService.controller;

import com.egs.ATMService.exception.NoAuthenticationHeadersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler for REST controllers
 *
 * @author Amir
 */
@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {


    public RestControllerExceptionHandler() {
        super();
    }

    /**
     * handles exceptions of type NoAuthenticationHeadersException.
     *
     * @param exception the exception thrown by any specific REST service.
     * @param request
     * @return a ResponseEntity<Object>
     */
    @ExceptionHandler({NoAuthenticationHeadersException.class})
    public ResponseEntity<Object> handleBadRequest(final NoAuthenticationHeadersException exception, final WebRequest request) {
        return handleExceptionInternal(exception, "No Authentication Header!", null, HttpStatus.OK, request);
    }


}
