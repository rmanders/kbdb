package org.schlocknet.kbdb.ws.interceptors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.schlocknet.kbdb.config.Constants.CHAR_ENCODING;
import org.schlocknet.kbdb.config.Constants.Errors;
import org.schlocknet.kbdb.config.Constants.Headers;
import org.schlocknet.kbdb.security.KbdbJWT;
import org.schlocknet.kbdb.security.KbdbJWTHeader;
import org.schlocknet.kbdb.security.KbdbJWTPayload;
import org.schlocknet.kbdb.services.SecurityService;
import org.schlocknet.kbdb.util.KbdbStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Ryan
 */
public class WebserviceAuthenticationInterceptor implements HandlerInterceptor {
    
    public static final Integer JWT_HEADER = 0;
    public static final Integer JWT_PAYLOAD = 1;
    public static final Integer JWT_SIGNATURE = 2;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Gson gson = new Gson();
    private final SecurityService sec;
    
    public WebserviceAuthenticationInterceptor(SecurityService sec) {
        this.sec = sec;
    }

    //<editor-fold defaultstate="collapsed" desc="preHandle">
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        final String authHeaderValue = request
                .getHeader(Headers.AUTH_JWT.getValue());
        
        // No header, no access
        if (authHeaderValue == null) {
            response.addHeader(Headers.AUTH_RESP.getValue(), "no auth header");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        
        // If invalid jwt format, no access
        String[] tokenParts = authHeaderValue.trim().split("\\.");
        if (tokenParts == null || tokenParts.length != 3) {
            response.addHeader(Headers.AUTH_RESP.getValue(),
                    "auth header is not jwt");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        
        // Decode the jwt
        KbdbJWT jwt = this.decodeKbdbJWT(
                tokenParts[JWT_HEADER],
                tokenParts[JWT_PAYLOAD],
                tokenParts[JWT_SIGNATURE]);
        
        // If we coudn't decode it into a KbdbJWT object
        if (jwt == null || jwt.getPayload() == null) {
            response.addHeader(Headers.AUTH_RESP.getValue(),
                    "could not decode jwt");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        
        // If the JWT is expired
        if (jwt.getPayload().getExp() == null
                || jwt.getPayload().getExp() < System.currentTimeMillis()) {
            response.addHeader(Headers.AUTH_RESP.getValue(), "expired");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        
        // If the signature is invalid
        if (!isValidSignedJwt(
                tokenParts[JWT_HEADER],
                tokenParts[JWT_PAYLOAD],
                jwt.getSignature())) {
            response.addHeader(Headers.AUTH_RESP.getValue(), "bad signature");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        
        // TODO: Check the requested method for AllowedRoles annotation
        
        return true;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="postHandle">
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        // Do nothing
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="afterCompletion">
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {
        // Do Nothing
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="isValidSignedJwt">
    /**
     *
     * Checks that the jwt has been signed by the server
     *
     * @param encodedHeader
     * @param encodedPayload
     * @param decodedSignature
     * @return
     * @throws UnsupportedEncodingException
     */
    private boolean isValidSignedJwt(
            String encodedHeader,
            String encodedPayload,
            byte[] decodedSignature) {
        
        try {
            // Check for null or empty value
            if (KbdbStrings.isBlank(encodedHeader)
                    || KbdbStrings.isBlank(encodedPayload)
                    || decodedSignature == null) {
                return false;
            }

            // Check the signature
            return sec.verifySignedString(
                    encodedHeader + "." + encodedPayload,
                    decodedSignature);
        } catch (UnsupportedEncodingException ex) {
            logger.error("{}: Error while validating signature of JWT",
                    Errors.CHAR_ENCODING, ex);
            return false;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="decodeKbdbJWT">
    /**
     * Decodes the three JWT sections into a KbdbJWT object.
     *
     * @param encodedHeader The base64 encoded header section of the JWT
     *
     * @param encodedPayload The base64 encoded payload section of the JWT
     *
     * @param encodedSignature The base64 encoded signature section of the JWT
     *
     * @return An instance of a {@link KbdbJWT} object or null of the jwt string
     * was invalid;
     */
    private KbdbJWT decodeKbdbJWT(
            String encodedHeader,
            String encodedPayload,
            String encodedSignature) {
        
        try {
            KbdbJWTHeader jwtHeader = gson.fromJson(
                    new String(
                            Base64.getDecoder().decode(encodedHeader),
                            CHAR_ENCODING),
                    KbdbJWTHeader.class
            );
            KbdbJWTPayload jwtPayLoad = gson.fromJson(
                    new String(
                            Base64.getDecoder().decode(encodedPayload),
                            CHAR_ENCODING),
                    KbdbJWTPayload.class
            );
            byte[] signature = Base64.getDecoder().decode(encodedSignature);
            KbdbJWT jwt = new KbdbJWT();
            jwt.setHead(jwtHeader);
            jwt.setPayload(jwtPayLoad);
            jwt.setSignature(signature);
            return jwt;
        } catch (UnsupportedEncodingException ex) {
            logger.error("{}: while decoding JWT from header",
                    Errors.CHAR_ENCODING,
                    ex);
            return null;
        } catch (JsonSyntaxException ex) {
            logger.error("Error while decoding jwt from JSON", ex);
            return null;
        } catch (IllegalArgumentException ex) {
            logger.error("Error decoding jwt from base64 encoding");
            return null;
        }
    }
    //</editor-fold>        
    
}
