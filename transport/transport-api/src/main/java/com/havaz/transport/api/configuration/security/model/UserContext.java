package com.havaz.transport.api.configuration.security.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class UserContext {
    private final Integer id;
    private final String username;
    private final List<GrantedAuthority> authorities;

    private UserContext(Integer id, String username, List<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }
    
    public static UserContext create(Integer id, String username, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username is blank: " + username);
        }
        return new UserContext(id, username, authorities);
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
