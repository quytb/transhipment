package com.havaz.transport.api.rest;

import com.havaz.transport.api.configuration.security.auth.JwtAuthenticationToken;
import com.havaz.transport.api.configuration.security.model.UserContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * End-point for retrieving logged-in user details.
 */
@RestController
public class ProfileRest {

    @RequestMapping(value = "/api/me", method = RequestMethod.GET)
    @ResponseBody
    public UserContext get(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
    }
}
