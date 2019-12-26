package com.havaz.transport.api.exception;

import com.havaz.transport.core.exception.TransportException;

/**
 * JwtTokenNotValid
 */
public class InvalidJwtToken extends TransportException {

    private static final long serialVersionUID = 8897196074271647570L;

    public InvalidJwtToken(String message) {
        super(message);
    }

    public InvalidJwtToken(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtToken(Throwable cause) {
        super(cause);
    }

    protected InvalidJwtToken(String message, Throwable cause, boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
