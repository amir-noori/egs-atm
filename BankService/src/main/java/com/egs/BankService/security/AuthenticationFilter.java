package com.egs.BankService.security;

import com.egs.BankService.common.model.AuthenticateUserCardRequest;
import com.egs.BankService.exception.CustomAuthenticationException;
import com.egs.BankService.service.api.BankCardDetailService;
import com.egs.BankService.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LogManager.getLogger(AuthenticationFilter.class);

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    private BankCardDetailService bankCardDetailService;
    private Environment environment;
    private JwtUtil jwtUtil;

    public AuthenticationFilter(BankCardDetailService bankCardDetailService,
                                Environment environment,
                                AuthenticationManager authenticationManager,
                                JwtUtil jwtUtil) {
        this.bankCardDetailService = bankCardDetailService;
        this.environment = environment;
        this.jwtUtil = jwtUtil;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {

            final String token = req.getHeader("token");
            final String cardNumberHeader = req.getHeader("cardNumber");
            if (token != null && token != "" && cardNumberHeader != null && cardNumberHeader != "") {
                if (jwtUtil.extractExpiration(token).before(Calendar.getInstance().getTime())) {
                    // token expired
                    throw new CustomAuthenticationException("token expired!");
                } else {
                    String extractedCardNumber = jwtUtil.extractUsername(token);
                    if (!cardNumberHeader.equals(extractedCardNumber)) {
                        throw new CustomAuthenticationException("wrong token!");
                    } else {
                        return new UsernamePasswordAuthenticationToken(extractedCardNumber, null, new ArrayList<>());
                    }
                }
            }

            try {
                AuthenticateUserCardRequest authenticateUserCardRequest = new ObjectMapper().readValue(req.getInputStream(), AuthenticateUserCardRequest.class);
                return getAuthenticationManager().authenticate(
                        new UsernamePasswordAuthenticationToken(authenticateUserCardRequest.getCardNumber(), authenticateUserCardRequest));

            } catch (MismatchedInputException e) {
                throw new CustomAuthenticationException("wrong headers!");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String token = "";
        Object principal = auth.getPrincipal();

        if (!isTokenValid(req)) {
            Date expireTime = new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("jwt.token.expiration.time")));

            token = Jwts.builder()
                    .setSubject(principal.toString())
                    .setExpiration(expireTime)
                    .signWith(SignatureAlgorithm.HS512, environment.getProperty("jwt.token.secret"))
                    .compact();
        } else {
            token = req.getHeader("token");
        }
        res.addHeader("token", token);
        res.addHeader("cardNumber", principal.toString());

    }

    private boolean isTokenValid(HttpServletRequest req) {
        final String token = req.getHeader("token");
        final String cardNumberHeader = req.getHeader("cardNumber");
        return jwtUtil.validateToken(token, cardNumberHeader);
    }


}
