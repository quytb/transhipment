package com.havaz.transport.api.utils;

import com.havaz.transport.api.configuration.security.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    public static final Integer ADMIN_ID = 1;

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static int getCurrentUserLogin() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null) {

                Object principal = authentication.getPrincipal();

                if (principal instanceof UserContext) {
                    UserContext userContext = ((UserContext) principal);
                    if (userContext.getId() == null) {
                        log.warn("user id is null");
                        return ADMIN_ID;
                    }
                    return userContext.getId();
                }
            }
            return ADMIN_ID;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ADMIN_ID;
        }
    }
}
