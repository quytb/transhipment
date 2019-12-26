//package com.havaz.transport.api.filter;
//
//import com.havaz.transport.api.common.CacheData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(urlPatterns = {"/fwb/*"})
//public class IntegrationFilter implements Filter {
//    private static final Logger log = LoggerFactory.getLogger(IntegrationFilter.class);
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//
//        boolean isAuth = false;
//        isAuth = true;
//        log.info("Start check FWB authorization.");
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//
//        String appId = httpServletRequest.getHeader("app-id");
//        String appToken = httpServletRequest.getHeader("app-token");
//        log.info("============ Request URL:: " + httpServletRequest.getRequestURL());
//        log.info("============ app-id:: " + appId);
//        log.info("============ app-token:: " + appToken);
//        try {
//            if (appId != null && appToken != null) {
//                String appTokenDB = CacheData.CONFIGURATION_DATA.get(appId);
//                if (appTokenDB != null) {
//                    log.info("Token in DB: " + appTokenDB);
//                    if (appToken.equals(appTokenDB)) {
//                        isAuth = true;
//                    } else {
//                        isAuth = false;
//                    }
//                } else {
//                    isAuth = false;
//                    log.info("App don't existing.");
//                }
//            }
//        }catch (Exception e){
//            isAuth = false;
//            log.error("Authorization FWB false.");
//        }
//        isAuth = true;
//        if (isAuth) {
//            log.info("Authorization FWB successfully.");
//            chain.doFilter(request, response);
//        } else {
//            log.error("Authorization FWB false.");
//            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization FWB false.");
//        }
//    }
//}
