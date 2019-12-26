package com.havaz.transport.api.configuration.security.model.token;

import com.havaz.transport.api.configuration.security.model.Scopes;
import com.havaz.transport.api.exception.JwtExpiredTokenException;
import com.havaz.transport.core.utils.SpringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.BadCredentialsException;

import java.security.Key;
import java.util.List;
import java.util.Optional;

/**
 * RefreshToken
 */
@SuppressWarnings("unchecked")
public class RefreshToken implements JwtToken {
    private Jws<Claims> claims;

    private RefreshToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    /**
     * Creates and validates Refresh token 
     * 
     * @param token
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     * @return
     */
    public static Optional<RefreshToken> create(RawAccessJwtToken token) {
        Key signingKey = SpringUtil.getBean("jwtKey", Key.class);
        Jws<Claims> claims = token.parseClaims(signingKey);

        List<String> scopes = claims.getBody().get(JwtToken.SCOPES, List.class);
        if (scopes == null || scopes.isEmpty() 
                || scopes.stream().noneMatch(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope))) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(claims));
    }

    @Override
    public String getToken() {
        return null;
    }

    public Jws<Claims> getClaims() {
        return claims;
    }
    
    public String getJti() {
        return claims.getBody().getId();
    }
    
    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
