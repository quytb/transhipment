package com.havaz.transport.api.configuration.security.model;

/**
 * Scopes
 */
public enum Scopes {
    REFRESH_TOKEN;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
