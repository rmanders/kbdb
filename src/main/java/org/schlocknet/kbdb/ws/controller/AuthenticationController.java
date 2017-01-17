package org.schlocknet.kbdb.ws.controller;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import static org.schlocknet.kbdb.config.Constants.EMAIL_FROM;
import org.schlocknet.kbdb.config.Constants.Errors;
import org.schlocknet.kbdb.config.Constants.Headers;
import static org.schlocknet.kbdb.config.Constants.JWT_EXPIRE_OFFSET;
import org.schlocknet.kbdb.model.ResponseMessage;
import org.schlocknet.kbdb.model.User;
import org.schlocknet.kbdb.security.KbdbJWT;
import org.schlocknet.kbdb.security.KbdbJWTPayload;
import org.schlocknet.kbdb.services.DataAccessService;
import org.schlocknet.kbdb.services.SecurityService;
import org.schlocknet.kbdb.util.KbdbStrings;
import org.schlocknet.kbdb.ws.requests.UserEmailRequest;
import org.schlocknet.kbdb.ws.requests.UsernameAndPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
        final User user;
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
            jwtPayload.setUserUUID(user.getUserUUID());
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
    
    //<editor-fold defaultstate="collapsed" desc="passwordResetRequest">
    /**
     * Generate a password reset and send an email to the user who requested
     * the reset
     *
     * @param req
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(
            value="/password/forgot",
            method=RequestMethod.POST,
            consumes="application/json",
            produces="application/json")
    public ResponseMessage<Object> passwordResetRequest(
            @RequestBody(required=true) UserEmailRequest req,
            HttpServletResponse response) {
        logger.debug("Got forgot password request");
        if (!KbdbStrings.isValidEmail(req.getEmailAddress())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseMessage<>(false, "malformed email");
        }
        
        // Check that the user email exists in database
        final User user;
        try {
            user = das.getUserDao().getByEmailAddress(req.getEmailAddress());
            if (user == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseMessage<>(false, "invalid email address");
            }
        } catch (Exception ex) {
            logger.error("{}: Error while getting user for password reset for "
                    + "emailAddress: {}", Errors.DB_USER_GET,
                    req.getEmailAddress(), ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        
        // Set a new verification hash for the user and save
        user.setVerificationToken(KbdbStrings.randomString());
        try {
            das.getUserDao().update(user);
        } catch (Exception ex) {
            logger.error("{}: Error while updating user for password reset for "
                    + "emailAddress: {}", Errors.DB_USER_UPDATE,
                    req.getEmailAddress(), ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        
        // Send reset email to the user
        sendPasswordResetEmail(user);
        
        return new ResponseMessage<>(true, "Generated password reset request. "
                + "Please check your email.");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="passwordResetExternal">
    /**
     * Resets the user's password from a password reset request
     *
     * @param userUuid
     * @param token
     * @param req
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(
            value="/password/tokenreset",
            method=RequestMethod.POST,
            consumes="application/json",
            produces="application/json")
    public ResponseMessage<Object> passwordResetExternal(
            @RequestParam(value="uuid", required=true) UUID userUuid,
            @RequestParam(value="token", required=true) String token,
            @RequestBody(required=true) UsernameAndPasswordRequest req,
            HttpServletResponse response) {
        
        // validation
        if (userUuid == null || KbdbStrings.isBlank(token) || req == null
                || !KbdbStrings.isValidEmail(req.getEmailAddress())
                || KbdbStrings.isBlank(req.getPassword())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseMessage<>(false, "invalid parameter in request");
        }
        
        // Get the user Object
        final User user;
        try {
            user = das.getUserDao().getByEmailAddress(req.getEmailAddress());
            if (user == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseMessage<>(false, "invalid email address");
            }
        } catch (Exception ex) {
            logger.error("{}: Error while getting user for password reset for "
                    + "emailAddress: {}", Errors.DB_USER_GET,
                    req.getEmailAddress(), ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        
        // Update the user's password
        user.setVerificationToken(KbdbStrings.randomString());
        user.setSaltBytes(sec.generateSalt());
        try {
            user.setPassword(sec.hashPassword(
                    req.getPassword(),
                    user.getSaltBytes()));
        } catch (UnsupportedEncodingException ex) {
            logger.error("{}: Encoding error while setting users new password",
                    Errors.CHAR_ENCODING, ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        try {
            das.getUserDao().update(user);
        } catch (Exception ex) {
            logger.error("{}: Error while updating user for password reset for "
                    + "emailAddress: {}", Errors.DB_USER_UPDATE,
                    req.getEmailAddress(), ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        
        return new ResponseMessage<>(true, "password reset successful");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="passwordResetInternal">
    /**
     * Resets a user's password from an internal (already authenticated) request
     *
     * @param userUUID
     * @param req
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(
            value="/password/reset/{userUUID}",
            method=RequestMethod.POST,
            produces="application/json",
            consumes="application/json")
    public ResponseMessage<Object> passwordResetInternal(
            @PathVariable("userUUID") UUID userUUID,
            @RequestBody(required=true) UsernameAndPasswordRequest req,
            HttpServletResponse response) {
        logger.debug("Got request to reset password (internal)");
        
        // TODO: check for emailAddress equals users UUID
        // validation
        if (req == null || KbdbStrings.isBlank(req.getPassword())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseMessage(false, "invalid password");
        }
        
        // Get the user Object
        final User user;
        try {
            user = das.getUserDao().getByUuid(userUUID);
            if (user == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseMessage<>(false, "invalid user UUID");
            }
        } catch (Exception ex) {
            logger.error("{}: Error while getting user for password reset for "
                    + "userUUID: {}", Errors.DB_USER_GET,
                    userUUID, ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        
        // Update the user's password
        user.setVerificationToken(KbdbStrings.randomString());
        user.setSaltBytes(sec.generateSalt());
        try {
            user.setPassword(sec.hashPassword(
                    req.getPassword(),
                    user.getSaltBytes()));
        } catch (UnsupportedEncodingException ex) {
            logger.error("{}: Encoding error while setting users new password",
                    Errors.CHAR_ENCODING, ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        try {
            das.getUserDao().update(user);
        } catch (Exception ex) {
            logger.error("{}: Error while updating user for password reset for "
                    + "userUUID: {}", Errors.DB_USER_UPDATE,
                    userUUID, ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "internal server error");
        }
        
        return new ResponseMessage<>(true, "password reset successful");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="sendPasswordResetEmail">
    /**
     * Sends a password reset email to the emailAddress of the user object
     *
     * @param user The user object to send a password email to
     */
    private void sendPasswordResetEmail(User user) {
        if (user == null || user.getEmailAddress() == null) {
            logger.warn("User instance or email was null");
            return;
        }
        
        // Set the body of the email
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.kbdb.io/auth/password/tokenreset?uuid=")
                .append(user.getUserUUID())
                .append("&token=")
                .append(user.getVerificationToken());
        
        try {
            Properties props = new Properties();
            Session session  = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(EMAIL_FROM));
            msg.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(user.getEmailAddress())
            );
            msg.setSubject("kbdb.io password reset");
            msg.setText(sb.toString());
            Transport.send(msg);
        } catch (MessagingException ex) {
            logger.error("{}: Error while sending password reset email to: {}",
                    Errors.EMAIL_SEND, user.getEmailAddress(), ex);
            // TODO: Do something else here?
        }
    }
    //</editor-fold>
}
