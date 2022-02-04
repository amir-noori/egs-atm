package com.egs.BankService.security;

import com.egs.BankService.service.api.BankCardDetailService;
import com.egs.BankService.util.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private static final Logger log = LogManager.getLogger(WebSecurityConfigurer.class);


    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment environment;
    private final BankCardDetailService bankCardDetailService;
    private final CustomAuthenticationProvider authProvider;
    private final JwtUtil jwtUtil;
    private final JwtRequestFilter jwtRequestFilter;


    @Autowired
    public WebSecurityConfigurer(BCryptPasswordEncoder bCryptPasswordEncoder,
                                 Environment environment,
                                 BankCardDetailService bankCardDetailService,
                                 CustomAuthenticationProvider authProvider,
                                 JwtUtil jwtUtil,
                                 JwtRequestFilter jwtRequestFilter) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.bankCardDetailService = bankCardDetailService;
        this.authProvider = authProvider;
        this.jwtUtil = jwtUtil;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/bank/service/authenticate").permitAll().
                anyRequest().authenticated().and().
                exceptionHandling().and().addFilter(getAuthenticationFilter()).sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(bankCardDetailService, environment, authenticationManager(), jwtUtil);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("authenticate.url.path"));
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(bankCardDetailService).passwordEncoder(bCryptPasswordEncoder);
    }


}
