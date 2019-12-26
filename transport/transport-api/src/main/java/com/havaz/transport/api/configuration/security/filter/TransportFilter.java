//package com.havaz.transport.api.filter;
//
//import com.havaz.transport.dao.entity.AdminLv2UserEntity;
//import com.havaz.transport.api.service.AdminLv2UserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.context.support.SpringBeanAutowiringSupport;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(urlPatterns = {"/taixe/*"})
//public class TransportFilter implements Filter {
//    @Autowired
//    private AdminLv2UserService adminLv2UserService;
//
//    private static final Logger log = LoggerFactory.getLogger(TransportFilter.class);
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//        boolean isAuth = false;
//        isAuth = true;
//        log.info("Start check authentication.");
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//
//        String userId = httpServletRequest.getHeader("user-id");
//        String authToken = httpServletRequest.getHeader("auth-token");
//        log.info("============ Request URL:: " + httpServletRequest.getRequestURL());
//        log.info("============ user-id:: " + userId);
//        log.info("============ auth-token:: " + authToken);
//        try {
//            if (adminLv2UserService == null) {
//                log.info("init adminLv2UserService.");
//                ServletContext context = httpServletRequest.getSession().getServletContext();
//                SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, context);
//            }
//            if (userId != null && authToken != null) {
//                AdminLv2UserEntity adminLv2UserEntity = adminLv2UserService.getByAdmId(Integer.parseInt(userId));
//                if (adminLv2UserEntity != null) {
//                    String tokenDB = adminLv2UserEntity.getAdmToken();
//                    log.info("Token in DB: " + tokenDB);
//                    if (tokenDB != null && authToken.equals(tokenDB)) {
//                        isAuth = true;
//                    } else {
//                        isAuth = false;
//                    }
//                } else {
//                    isAuth = false;
//                    log.info("User don't existing.");
//                }
//            }
//        }catch (Exception e){
//            isAuth = false;
//            log.error("Error authentication.", e);
//        }
//        isAuth = true;
//        if (isAuth) {
//            log.info("Authentication successfully.");
//            chain.doFilter(request, response);
//        } else {
//            log.error("Authentication false.");
//            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "User không có quyền truy cập");
//        }
//    }
//
//}
