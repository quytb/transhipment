package com.havaz.transport.api.configuration.security.auth.jwt.extractor;

/**
 * Implementations of this interface should always return raw base-64 encoded
 * representation of JWT Token.
 */
public interface TokenExtractor {
    String extract(String payload);
}
