package com.havaz.transport.api.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class AuthMethodNotSupportedException extends AuthenticationServiceException {

    private static final long serialVersionUID = 3673362052470649335L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
