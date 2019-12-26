package com.havaz.transport.api.configuration.security.model.token;

public interface JwtToken {
    String SCOPES = "scopes";
    String ID = "id";
    String ACCESS_TOKEN_PAYLOAD_KEY = "accessToken";
    String getToken();
}
