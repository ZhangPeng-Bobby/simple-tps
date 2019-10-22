package com.citi.group2.simpletps.Config;

import com.citi.group2.simpletps.annotation.CurrentTrader;
import com.citi.group2.simpletps.entity.Trader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Configuration
public class CurrentTraderArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(Trader.class)
                && methodParameter.hasParameterAnnotation(CurrentTrader.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Trader trader = (Trader) nativeWebRequest.getAttribute("currentUser", RequestAttributes.SCOPE_REQUEST);
        if(trader != null){
            return trader;
        }
        throw new MissingServletRequestPartException("currentUser");
    }
}
