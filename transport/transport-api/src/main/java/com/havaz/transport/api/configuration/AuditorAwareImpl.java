package com.havaz.transport.api.configuration;

import com.havaz.transport.api.configuration.security.model.UserContext;
import com.havaz.transport.api.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Slf4j
@Configuration
public class AuditorAwareImpl implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserContext) {
                UserContext userContext = ((UserContext) principal);
                username = userContext.getUsername();
                if (StringUtils.isBlank(username)) {
                    throw new UsernameNotFoundException("username is not blank");
                }
                if (userContext.getId() == null) {
                    log.warn("user id is null");
                    return Optional.of(SecurityUtils.ADMIN_ID);
                }
                return Optional.of(userContext.getId());
            }
        }
        return Optional.of(SecurityUtils.ADMIN_ID); // adminId
    }
}
