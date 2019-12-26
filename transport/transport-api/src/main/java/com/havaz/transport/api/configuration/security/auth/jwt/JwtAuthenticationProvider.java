package com.havaz.transport.api.configuration.security.auth.jwt;

import com.havaz.transport.api.configuration.security.auth.JwtAuthenticationToken;
import com.havaz.transport.api.configuration.security.model.UserContext;
import com.havaz.transport.api.configuration.security.model.token.JwtToken;
import com.havaz.transport.api.configuration.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link JwtToken} to perform authentication.
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    @Qualifier("jwtKey")
    private Key key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(key);
        String subject = jwsClaims.getBody().getSubject();
        Integer id = jwsClaims.getBody().get(JwtToken.ID, Integer.class);
        List<String> scopes = jwsClaims.getBody().get(JwtToken.SCOPES, List.class);
        List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserContext context = UserContext.create(id, subject, authorities);

        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
