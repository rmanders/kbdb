package org.schlocknet.kbdb.ws.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.schlocknet.kbdb.config.Constants.Headers;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rmanders
 */
public class WebserviceAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, 
            HttpServletResponse response, 
            Object handler) throws Exception {
        final String authHeaderValue = request
                .getHeader(Headers.AUTH_JWT.getValue());
        
        // No header, no access
        if (authHeaderValue == null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        
        // If invalid jwt format, no access
        String[] tokenParts = authHeaderValue.trim().split("\\.");
        if (tokenParts == null || tokenParts.length != 3) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, 
            HttpServletResponse response, 
            Object handler, 
            ModelAndView modelAndView) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, 
            HttpServletResponse response, 
            Object handler, 
            Exception ex) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
