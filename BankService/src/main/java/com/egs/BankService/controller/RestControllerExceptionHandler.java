package com.egs.BankService.controller;

import com.egs.BankService.common.model.BankTransactionResponse;
import com.egs.BankService.exception.BankTransactionException;
import com.egs.BankService.exception.CustomAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler for bank REST services.
 *
 * @author Amir
 */
@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {


    public RestControllerExceptionHandler() {
        super();
    }

    /**
     * handles exceptions of type BankTransactionException
     *
     * @param exception the exception thrown in the REST controller.
     * @param request
     * @return a ResponseEntity<Object>
     */
    @ExceptionHandler({BankTransactionException.class})
    public ResponseEntity<Object> handleBadRequest(final BankTransactionException exception, final WebRequest request) {
        String message = "Error in Transaction: \n" + "Bank Card: " + exception.getBankCard().getCardNumber() + "\n" +
                "Error Description: " + exception.getDescription();
        BankTransactionResponse response = new BankTransactionResponse();
        response.setSuccessful(false);
        response.setResponseMessage(message);
        return handleExceptionInternal(exception, response, null, HttpStatus.OK, request);
    }

    /**
     * handles exceptions of type CustomAuthenticationException
     *
     * @param exception exception the exception thrown in the REST controller.
     * @param request
     * @return a ResponseEntity<Object>
     */
    @ExceptionHandler({CustomAuthenticationException.class})
    public ResponseEntity<Object> handleBadRequest(final CustomAuthenticationException exception, final WebRequest request) {
        String message = "hmm... something is wrong!";
        BankTransactionResponse response = new BankTransactionResponse();
        response.setSuccessful(false);
        response.setResponseMessage(message);
        return handleExceptionInternal(exception, response, null, HttpStatus.OK, request);
    }


}
