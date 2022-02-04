package com.egs.ATMService.controller;

import com.egs.ATMService.common.model.*;
import com.egs.ATMService.exception.NoAuthenticationHeadersException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * The entry point for ATM services.
 *
 * @author Amir
 */
@RestController
@RequestMapping("/atm/service")
public class AtmController {

    private static final Logger log = LogManager.getLogger(AtmController.class);


    /**
     * rest tempale to consume bank services
     */
    final RestTemplate restTemplate;


    final Environment environment;

    @Autowired
    public AtmController(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }


    /**
     * Resilience4J method for authenticateUserCard rest service.
     *
     * @param exception the exception accured while calling the service
     * @return a AuthenticateUserCardResponse
     */
    public AuthenticateUserCardResponse bankAuthenticationIssue(Throwable exception) {
        log.warn("There is an issue with the bank authentication service: " + exception.getMessage());
        String message = "Bank authentication service is down momentarily. Please try later.";
        AuthenticateUserCardResponse response = new AuthenticateUserCardResponse();
        response.setValid(false);
        response.setResponseCode(0);
        response.setResponseMessage(message);
        return response;
    }

    /**
     * Resilience4J method for ban transaction rest services.
     *
     * @param exception the exception accured while calling the service
     * @return a BankTransactionResponse
     */
    public BankTransactionResponse bankTransactionIssue(Throwable exception) {
        log.warn("There is an issue with the bank authentication service: " + exception.getMessage());
        String message = "Bank service is down momentarily. Please try later.";
        BankTransactionResponse response = new BankTransactionResponse();
        response.setSuccessful(false);
        response.setResponseCode(100);
        response.setResponseMessage(message);
        return response;
    }

    /**
     * simple method to check if service is up.
     *
     * @return
     */
    @GetMapping("/test")
    public String test() {
        return "ATM is OK.";
    }

    /**
     * REST service to authenticate bank card with PIN code or finger print.
     *
     * @param request
     * @param httpServletRequest
     * @return a AuthenticateUserCardResponse
     */
    @PostMapping("/authenticate")
    @CircuitBreaker(name = "authenticateUserCard", fallbackMethod = "bankAuthenticationIssue")
    public AuthenticateUserCardResponse authenticateUserCard(@Valid @RequestBody AuthenticateUserCardRequest request,
                                                             HttpServletRequest httpServletRequest) {

        log.debug("begin authenticating for card number: " + request.getCardNumber());
        String bankAuthUrl = environment.getProperty("bank.url.auth");
        InitializeBankCardResponse initializeBankCardResponse = initializeConnection(new InitializeBankCardRequest(), httpServletRequest);
        if (initializeBankCardResponse != null && initializeBankCardResponse.getValid()) {
            ResponseEntity<AuthenticateUserCardResponse> authResponseEntity = restTemplate.exchange(
                    bankAuthUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, null),
                    new ParameterizedTypeReference<>() {
                    });

            HttpHeaders authResponseEntityHeaders = authResponseEntity.getHeaders();
            List<String> tokenHeaderList = authResponseEntityHeaders.get("token");
            List<String> cardNumberHeaderList = authResponseEntityHeaders.get("cardNumber");
            String token = "";
            if (tokenHeaderList != null && tokenHeaderList.size() == 1) {
                token = tokenHeaderList.get(0);
            }
            String cardNumber = "";
            if (cardNumberHeaderList != null && cardNumberHeaderList.size() == 1) {
                cardNumber = cardNumberHeaderList.get(0);
            }
            HttpSession httpSession = httpServletRequest.getSession();
            httpServletRequest.getSession().setAttribute("token", token);
            httpSession.setAttribute("cardNumber", cardNumber);
            log.debug("authentication is done.");
            return authResponseEntity.getBody();
        }

        return null;
    }

    /**
     * REST service to initialze and validate card for transactions
     *
     * @param request
     * @param httpServletRequest
     * @return a InitializeBankCardResponse
     */
    @PostMapping("/initialize")
    public InitializeBankCardResponse initializeConnectionWithBank(InitializeBankCardRequest request, HttpServletRequest httpServletRequest) {
        return initializeConnection(request, httpServletRequest);
    }

    private InitializeBankCardResponse initializeConnection(InitializeBankCardRequest request, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        Object initialized = httpSession.getAttribute("initialized");
        if (initialized != null) {
            // session is already initialized
            log.debug("connection is already initialized.");
            return new InitializeBankCardResponse(true);
        } else {
            InitializeBankCardResponse initializeBankCardResponse = sendInitializeConnectionRequest(request);
            httpSession.setAttribute("initialized", true);
            return initializeBankCardResponse;
        }
    }

    private InitializeBankCardResponse sendInitializeConnectionRequest(InitializeBankCardRequest request) {
        String bankInitUrl = environment.getProperty("bank.url.init");
        ResponseEntity<InitializeBankCardResponse> initResponseEntity = restTemplate.exchange(
                bankInitUrl,
                HttpMethod.POST,
                new HttpEntity<>(request, null),
                new ParameterizedTypeReference<>() {
                });
        return initResponseEntity.getBody();
    }


    /**
     * REST service to check the card balance.
     *
     * @param request
     * @param httpServletRequest
     * @return
     * @throws NoAuthenticationHeadersException when there is no JWT token or invalid token.
     */
    @PostMapping("/balance")
    @CircuitBreaker(name = "checkBalance", fallbackMethod = "bankTransactionIssue")
    public BankTransactionResponse checkBalance(@Valid @RequestBody BankTransactionRequest request, HttpServletRequest httpServletRequest) throws NoAuthenticationHeadersException {
        String bankBalanceUrl = environment.getProperty("bank.url.balance");
        ResponseEntity<BankTransactionResponse> initResponseEntity = restTemplate.exchange(
                bankBalanceUrl,
                HttpMethod.POST,
                new HttpEntity<>(request, getAuthenticationHeaders(httpServletRequest)),
                new ParameterizedTypeReference<>() {
                });
        return initResponseEntity.getBody();
    }

    /**
     * REST service to deposit money to bank card.
     *
     * @param request
     * @param httpServletRequest
     * @return
     * @throws NoAuthenticationHeadersException when there is no JWT token or invalid token.
     */
    @PostMapping("/deposit")
    @CircuitBreaker(name = "deposit", fallbackMethod = "bankTransactionIssue")
    public BankTransactionResponse deposit(@Valid @RequestBody BankTransactionRequest request, HttpServletRequest httpServletRequest) throws NoAuthenticationHeadersException {
        String bankDepositUrl = environment.getProperty("bank.url.deposit");
        ResponseEntity<BankTransactionResponse> initResponseEntity = restTemplate.exchange(
                bankDepositUrl,
                HttpMethod.POST,
                new HttpEntity<>(request, getAuthenticationHeaders(httpServletRequest)),
                new ParameterizedTypeReference<>() {
                });
        return initResponseEntity.getBody();
    }

    /**
     * REST service to withdrawl money from bank card.
     *
     * @param request
     * @param httpServletRequest
     * @return
     * @throws NoAuthenticationHeadersException when there is no JWT token or invalid token.
     */
    @PostMapping("/withdrawal")
    @CircuitBreaker(name = "withdrawal", fallbackMethod = "bankTransactionIssue")
    public BankTransactionResponse withdrawal(@Valid @RequestBody BankTransactionRequest request, HttpServletRequest httpServletRequest) throws NoAuthenticationHeadersException {
        String bankWithdrawalUrl = environment.getProperty("bank.url.withdrawal");
        ResponseEntity<BankTransactionResponse> initResponseEntity = restTemplate.exchange(
                bankWithdrawalUrl,
                HttpMethod.POST,
                new HttpEntity<>(request, getAuthenticationHeaders(httpServletRequest)),
                new ParameterizedTypeReference<>() {
                });
        return initResponseEntity.getBody();
    }

    /**
     * helper method to provide the required headers for authentication.
     *
     * @param httpServletRequest
     * @return
     * @throws NoAuthenticationHeadersException when there is no JWT token or invalid token.
     */
    private HttpHeaders getAuthenticationHeaders(HttpServletRequest httpServletRequest) throws NoAuthenticationHeadersException {
        HttpSession session = httpServletRequest.getSession();
        Object tokenAttribute = session.getAttribute("token");
        Object cardNumberAttribute = session.getAttribute("cardNumber");
        HttpHeaders httpHeaders = new HttpHeaders();
        if (tokenAttribute != null) {
            String token = tokenAttribute.toString();
            if (!token.equals("")) {
                httpHeaders.add("token", token);
            } else {
                throw new NoAuthenticationHeadersException();
            }
        } else {
            throw new NoAuthenticationHeadersException();
        }
        if (cardNumberAttribute != null) {
            String cardNumber = cardNumberAttribute.toString();
            if (!cardNumber.equals("")) {
                httpHeaders.add("cardNumber", cardNumber);
            } else {
                throw new NoAuthenticationHeadersException();
            }
        } else {
            throw new NoAuthenticationHeadersException();
        }
        return httpHeaders;
    }


}
