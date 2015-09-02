package org.schlocknet.kbdb.ws;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletResponse;
import org.schlocknet.kbdb.config.Constants.Errors;
import org.schlocknet.kbdb.config.Constants.Headers;
import static org.schlocknet.kbdb.config.Constants.JWT_EXPIRE_OFFSET;
import org.schlocknet.kbdb.model.ResponseMessage;
import org.schlocknet.kbdb.model.UserModel;
import org.schlocknet.kbdb.security.KbdbJWT;
import org.schlocknet.kbdb.security.KbdbJWTPayload;
import org.schlocknet.kbdb.services.DataAccessService;
import org.schlocknet.kbdb.services.SecurityService;
import org.schlocknet.kbdb.ws.requests.UsernameAndPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ryan
 * 
 * Authentication endpoint for web services
 * 
 */
@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    
    private static final String INVALID_MSG = 
            "Invalid emailAddress/username or password";
    
    private static final String SERVER_ERROR_MSG = 
            "Internal server error";    
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    SecurityService sec;
    
    @Autowired
    DataAccessService das;

    //<editor-fold defaultstate="collapsed" desc="doLogin">
    /**
     * Authenticates a user and if successful, issues a JWT in the response
     * header.
     *
     * @param req JSON request body containing the username and password
     *
     * @param response HttpServletResponse
     *
     * @return
     */
    @RequestMapping(
            value="/login",
            method=RequestMethod.POST,
            consumes="application/json",
            produces="application/json")
    @ResponseBody
    public ResponseMessage<String> doLogin(
            @RequestBody UsernameAndPasswordRequest req,
            HttpServletResponse response) {
        logger.debug("Got login request.");
        
        if (req == null || !req.isValid()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseMessage<>(false, INVALID_MSG);
        }
        
        // Look for the user
        final UserModel user;
        try {
            user = das.getUserDao().getByEmailAddress(req.getEmailAddress());
            if (user == null) {
                return new ResponseMessage<>(false, INVALID_MSG);
            }
        } catch (Exception ex) {
            logger.error("{}: during user login for user with emailAddress: {}",
                    Errors.DB_USER_GET, req.getEmailAddress(), ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, SERVER_ERROR_MSG);
        }
        
        // Check that the password is valid
        try {
            if (!sec.verifyPassword(req.getPassword(), user.getSaltBytes(),
                    user.getPassword())) {
                return new ResponseMessage<>(false, INVALID_MSG);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error("{}: Encoding error while checking user's password "
                    + "during login", Errors.CHAR_ENCODING, ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, SERVER_ERROR_MSG);
        }
        
        // Password is valid. Issue a JWT
        KbdbJWTPayload jwtPayload = new KbdbJWTPayload();
        KbdbJWT jwt = new KbdbJWT();
        try {
            jwtPayload.setEmailAddress(req.getEmailAddress());
            jwtPayload.setExp(System.currentTimeMillis() + JWT_EXPIRE_OFFSET);
            jwtPayload.setRoles(user.getRoles());
            jwt.setPayload(jwtPayload);
            jwt.setSignature(sec.sign(jwt.encodeHeaderAndPayload()));
            String encodedJwt = jwt.encode();
            response.setHeader(Headers.AUTH_JWT.getValue(), encodedJwt);
            response.setHeader(Headers.AUTH_RESP.getValue(), "");
            return new ResponseMessage(true, null, encodedJwt);
        } catch (UnsupportedEncodingException ex) {
            logger.error("{}: Character encoding error while generating JWT "
                    + "for sucessful login for emailAddress: {}",
                    Errors.CHAR_ENCODING,
                    req.getEmailAddress(),
                    ex);
            return new ResponseMessage(false, SERVER_ERROR_MSG);
        }
    }
    //</editor-fold>
    
}
