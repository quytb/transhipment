package com.havaz.transport.api.configuration.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.common.ErrorCode;
import com.havaz.transport.api.common.ErrorResponse;
import com.havaz.transport.api.exception.AuthMethodNotSupportedException;
import com.havaz.transport.api.exception.JwtExpiredTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(AjaxAwareAuthenticationFailureHandler.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public AjaxAwareAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        if (log.isDebugEnabled()) {
            log.debug("request-uri: {}", request.getRequestURI());
            log.debug("Authentication failed", e);
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            objectMapper.writeValue(response.getWriter(), ErrorResponse
                    .of(e.getMessage(), ErrorCode.AUTHENTICATION,
                        HttpStatus.UNAUTHORIZED));
        } else if (e instanceof UsernameNotFoundException) {
            objectMapper.writeValue(response.getWriter(), ErrorResponse
                    .of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        } else if (e instanceof JwtExpiredTokenException) {
            objectMapper.writeValue(response.getWriter(), ErrorResponse
                    .of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED,
                        HttpStatus.UNAUTHORIZED));
        } else if (e instanceof AuthMethodNotSupportedException) {
            objectMapper.writeValue(response.getWriter(), ErrorResponse
                    .of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        } else if (e instanceof InsufficientAuthenticationException) {
            objectMapper.writeValue(response.getWriter(), ErrorResponse
                    .of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        } else {
            objectMapper.writeValue(response.getWriter(),
                                    ErrorResponse.of("Authentication failed",
                                                     ErrorCode.AUTHENTICATION,
                                                     HttpStatus.UNAUTHORIZED));
        }
    }
}
