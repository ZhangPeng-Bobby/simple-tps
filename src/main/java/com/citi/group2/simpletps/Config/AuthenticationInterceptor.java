package com.citi.group2.simpletps.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.citi.group2.simpletps.annotation.LoginRequired;
import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.service.TraderService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private TraderService traderService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // pass if not method
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        if (methodAnnotation != null) {
            // Authenticate
            String token = request.getHeader("token");  // extract token from header
            if (token == null) {
                throw new RuntimeException("无token，请重新登录");
            }
            int userId;
            try {
                userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));  // 获取 token 中的 user id
            } catch (JWTDecodeException e) {
                throw new RuntimeException("token无效，请重新登录");
            }
            if (traderService == null) {//try to new a traderService
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                traderService = (TraderService) factory.getBean("traderService");
            }
            Trader trader = traderService.findById(userId);
            if (trader == null) {
                throw new RuntimeException("用户不存在，请重新登录");
            }
            // Authenticate token
            try {
                JWTVerifier verifier =  JWT.require(Algorithm.HMAC256(trader.gettPwd())).build();
                try {
                    verifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("token无效，请重新登录");
                }
            } catch (UnsupportedEncodingException ignore) {}
            request.setAttribute("currentUser", trader);
            return true;
        }
        return true;
    }
}
