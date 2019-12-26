package com.havaz.transport.api.configuration.security.auth.jwt.verifier;

public interface TokenVerifier {
    boolean verify(String jti);
}
