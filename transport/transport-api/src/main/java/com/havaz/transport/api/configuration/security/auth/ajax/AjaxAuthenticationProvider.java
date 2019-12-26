package com.havaz.transport.api.configuration.security.auth.ajax;

import com.havaz.transport.api.configuration.security.model.UserContext;
import com.havaz.transport.api.service.AdminLv2UserService;
import com.havaz.transport.dao.entity.AdminLv2GroupEntity;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.TcPermissionEntity;
import com.havaz.transport.dao.entity.TcRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    public static final int ERP_ADMIN_GROUP_TRUNG_CHUYEN_ID = 17;
    public static final int ERP_MODULE_TRUNG_CHUYEN_ID = 198;
    public static final String MESSAGE_ERP_GROUP_PERMISSION = "Người dùng không thuộc nhóm Điều hành trung chuyển";
    private static final String MESSAGE_BAD_CREDENTIALS_EXCEPTION = "Tên đăng nhập hoặc mật khẩu không hợp lệ";
    private static final String MESSAGE_USERNAME_NOT_FOUND_EXCEPTION = "Người dùng không tồn tại";

    @Autowired
    private AdminLv2UserService adminLv2UserService;

    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        AdminLv2UserEntity user = adminLv2UserService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MESSAGE_USERNAME_NOT_FOUND_EXCEPTION + ": " + username);
        }

        if (!user.authenticate(password)) {
            throw new BadCredentialsException(MESSAGE_BAD_CREDENTIALS_EXCEPTION);
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
                .filter(TcPermissionEntity::getEnabled)
                .map(e -> new SimpleGrantedAuthority(e.getCode().getCode()))
                .collect(Collectors.toSet()));

        set.addAll(user.getPermissions().stream().filter(e -> e.getEnabled() && e.getCode() != null)
                           .map(e -> new SimpleGrantedAuthority(e.getCode().getCode()))
                           .collect(Collectors.toSet()));

        // TODO Uncomment if it's ready for production
        /*if (set.isEmpty()) {
            throw new InsufficientAuthenticationException("User doesn't have any privileges");
        }*/

        List<GrantedAuthority> authorities = new ArrayList<>(set);

        UserContext userContext = UserContext.create(user.getAdmId(), username, authorities);

        return new UsernamePasswordAuthenticationToken(userContext, null,
                                                       userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
