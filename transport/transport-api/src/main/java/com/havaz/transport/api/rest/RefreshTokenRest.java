package com.havaz.transport.api.rest;

import com.havaz.transport.api.configuration.security.WebSecurityConfig;
import com.havaz.transport.api.configuration.security.auth.jwt.extractor.TokenExtractor;
import com.havaz.transport.api.configuration.security.auth.jwt.verifier.TokenVerifier;
import com.havaz.transport.api.configuration.security.model.UserContext;
import com.havaz.transport.api.configuration.security.model.token.JwtToken;
import com.havaz.transport.api.configuration.security.model.token.JwtTokenFactory;
import com.havaz.transport.api.configuration.security.model.token.RawAccessJwtToken;
import com.havaz.transport.api.configuration.security.model.token.RefreshToken;
import com.havaz.transport.api.exception.InvalidJwtToken;
import com.havaz.transport.api.service.AdminLv2UserService;
import com.havaz.transport.dao.entity.AdminLv2GroupEntity;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.TcPermissionEntity;
import com.havaz.transport.dao.entity.TcRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.havaz.transport.api.configuration.security.auth.ajax.AjaxAuthenticationProvider.ERP_ADMIN_GROUP_TRUNG_CHUYEN_ID;
import static com.havaz.transport.api.configuration.security.auth.ajax.AjaxAuthenticationProvider.ERP_MODULE_TRUNG_CHUYEN_ID;
import static com.havaz.transport.api.configuration.security.auth.ajax.AjaxAuthenticationProvider.MESSAGE_ERP_GROUP_PERMISSION;

/**
 * RefreshTokenEndpoint
 */
@RestController
public class RefreshTokenRest {

    @Autowired
    private JwtTokenFactory tokenFactory;

    @Autowired
    private TokenVerifier tokenVerifier;

    @Autowired
    private AdminLv2UserService adminLv2UserService;

    @Autowired
    @Qualifier("jwtHeaderTokenExtractor")
    private TokenExtractor tokenExtractor;

    @RequestMapping(value = "/auth/token", method = RequestMethod.POST,
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public JwtToken refreshToken(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(
                WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken)
                .orElseThrow(() -> new InvalidJwtToken("Invalid jwt token"));

        // TODO refactor
        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken("Invalid jwt token");
        }

        String subject = refreshToken.getSubject();

        AdminLv2UserEntity user = adminLv2UserService.findByUsername(subject);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + subject);
        }

        AdminLv2GroupEntity group = user.getGroups().stream()
                .filter(e -> e.getId() == ERP_ADMIN_GROUP_TRUNG_CHUYEN_ID)
                .findAny()
                .orElseThrow(() -> new InsufficientAuthenticationException(
                        MESSAGE_ERP_GROUP_PERMISSION));

        group.getAdmRoles().stream()
                .filter(role -> role.getModuleId() == ERP_MODULE_TRUNG_CHUYEN_ID &&
                        (role.getRoleView() || role.getRoleAdd() || role.getRoleEdit() ||
                                role.getRoleDelete() || role.getRoleExport() || role.getRoleImport()))
                .findAny()
                .orElseThrow(() -> new InsufficientAuthenticationException(
                        MESSAGE_ERP_GROUP_PERMISSION));

        Set<GrantedAuthority> set = new HashSet<>();

        set.addAll(user.getRoles().stream().map(TcRoleEntity::getPermissions)
                           .flatMap(Collection::stream)
                           .filter(e -> e.getEnabled() && e.getCode() != null)
                           .map(e -> new SimpleGrantedAuthority(e.getCode().getCode()))
                           .collect(Collectors.toSet()));

        set.addAll(user.getPermissions().stream().filter(TcPermissionEntity::getEnabled)
                           .map(e -> new SimpleGrantedAuthority(e.getCode().getCode()))
                           .collect(Collectors.toSet()));


        // TODO Uncomment if it's ready for production
        /*if (set.isEmpty()) {
            throw new InsufficientAuthenticationException("User doesn't have any privileges");
        }*/

        List<GrantedAuthority> authorities = new ArrayList<>(set);

        UserContext userContext = UserContext.create(user.getAdmId(), subject, authorities);

        return tokenFactory.createAccessJwtToken(userContext);
    }
}
