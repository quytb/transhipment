package com.havaz.transport.api.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.configuration.security.auth.ajax.AjaxAuthenticationProvider;
import com.havaz.transport.api.configuration.security.auth.ajax.AjaxLoginProcessingFilter;
import com.havaz.transport.api.configuration.security.auth.jwt.JwtAuthenticationProvider;
import com.havaz.transport.api.configuration.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.havaz.transport.api.configuration.security.auth.jwt.SkipPathRequestMatcher;
import com.havaz.transport.api.configuration.security.auth.jwt.extractor.TokenExtractor;
import com.havaz.transport.api.configuration.security.filter.CustomCorsFilter;
import com.havaz.transport.core.constant.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    private static final String AUTHENTICATION_URL = "/login";
    private static final String REFRESH_TOKEN_URL = "/auth/token";
    private static final String API_ROOT_URL = "/**";

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint,
                                                                         successHandler,
                                                                         failureHandler,
                                                                         objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    private JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip,String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
                = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor,
                                                             matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays
                .asList(AUTHENTICATION_URL, REFRESH_TOKEN_URL, "/taixe/**", "/fwb/**", "/test/**",
                        "/public/**", "/location/**", "/havaznow/**", "/havaznowmockup/**",
                        "/error", "/error.*", "/socket/**", "/dieu-hanh-hub/**", "/v1/dieu-hanh-hub/**");

        // @formatter:off
        http.csrf().disable() // We don't need CSRF for JWT based authentication
            .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[0])).permitAll().and()
            .authorizeRequests()
                .antMatchers("/dieudo/**", "/benxe/**").hasAnyAuthority(UserPermission.P90000001.getCode(), UserPermission.P10000001.getCode())
                .antMatchers("/chamcong/**", "/vungtrungchuyen", "/vtc-ctv/**", "/ctv-price/**").hasAnyAuthority(UserPermission.P90000001.getCode(), UserPermission.P20000001.getCode())
                .antMatchers("/lichtruc/**", "/catruc/**").hasAnyAuthority(UserPermission.P90000001.getCode(), UserPermission.P10000001.getCode(), UserPermission.P20000001.getCode(), UserPermission.P30000001.getCode())
                .antMatchers("/baocaothang").hasAnyAuthority(UserPermission.P90000001.getCode(), UserPermission.P30000001.getCode())
                .antMatchers("/admin/**").hasAnyAuthority(UserPermission.P90000001.getCode())
            .and()
            .authorizeRequests()
                .anyRequest().authenticated().and()
            .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL),
                             UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, API_ROOT_URL),
                             UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                                   "/swagger-ui.html", "/webjars/**", "/favicon.ico");
    }
}
