package com.havaz.transport.api.configuration.security.model.token;

import com.havaz.transport.api.configuration.security.model.Scopes;
import com.havaz.transport.api.configuration.security.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Factory class that should be always used to create {@link JwtToken}.
 */
@Component
public class JwtTokenFactory {

    private static final String TOKEN_ISSUER = "Havaz Transport";

    @Autowired
    @Qualifier("jwtKey")
    private Key key;

    /**
     * Factory method for issuing new JWT Tokens.
     */
    public AccessJwtToken createAccessJwtToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        // TODO Uncomment if it's ready for production
        /*if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) {
            throw new IllegalArgumentException("User doesn't have any privileges");
        }*/

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put(JwtToken.ID, userContext.getId());
        claims.put(JwtToken.SCOPES, userContext.getAuthorities().stream().map(Object::toString)
                .collect(Collectors.toList()));

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(TOKEN_ISSUER)
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(
                        currentTime.plusHours(1L).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put(JwtToken.SCOPES, Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
        claims.put(JwtToken.ID, userContext.getId());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(TOKEN_ISSUER)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(
                        currentTime.plusMonths(6L).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new AccessJwtToken(token, claims);
    }
}
