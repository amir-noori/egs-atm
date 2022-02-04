package com.egs.BankService.controller;

import com.egs.BankService.common.model.BaseRequest;
import com.egs.BankService.exception.CustomAuthenticationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

@ControllerAdvice
public class ControllerAdvisor implements RequestBodyAdvice {

    private static final Logger log = LogManager.getLogger(ControllerAdvisor.class);


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        if (body instanceof BaseRequest) {
            long cardNumber = ((BaseRequest) body).getCardNumber();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String cardNumberHeader = request.getHeader("cardNumber");
            String token = request.getHeader("token");
            if (cardNumberHeader != null && cardNumber != Long.parseLong(cardNumberHeader)) {
                log.warn("The token seemed to be hijacked");
                log.warn("Token: " + token);
                log.warn("Card Number: " + cardNumber);
                throw new CustomAuthenticationException("card numbers dont match");
            }
        }

        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }
}
