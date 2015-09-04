package org.schlocknet.kbdb.ws;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.schlocknet.kbdb.annotations.AllowedRoles;
import org.schlocknet.kbdb.config.Constants.Errors;
import org.schlocknet.kbdb.config.Constants.Roles;
import org.schlocknet.kbdb.model.ResponseMessage;
import org.schlocknet.kbdb.model.UserModel;
import org.schlocknet.kbdb.services.DataAccessService;
import org.schlocknet.kbdb.services.SecurityService;
import org.schlocknet.kbdb.util.KbdbStrings;
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
 * @author rmanders
 */
@Controller
@RequestMapping("/users")
public class UserController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private DataAccessService das;
    
    @Autowired
    private SecurityService sec;
        
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET)
    public String testOutput() {
        return "Testing...";
    }
    
    //<editor-fold defaultstate="collapsed" desc="createNewUser">
    /**
     * Creates a new user account with the specified username and password.
     *
     * TODO: Add defense for Denial of Service attacks pounding the db
     *
     * @param newUserRequest
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST)
    public ResponseMessage<Object> createNewUser(
            @RequestBody UsernameAndPasswordRequest newUserRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        logger.debug("Got request for new user creation");
        
        // Validate new user request
        if (newUserRequest == null || !newUserRequest.isValid()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseMessage(false, "invalid username or password");
        }
        
        // Check for existing user with email in db
        final UserModel userChk;
        try {
            userChk = das.getUserDao().getByEmailAddress(
                    newUserRequest.getEmailAddress());
            if (userChk != null) {
                logger.warn("Tried to create new user with existing "
                        + "email address for emailAddress: {}",
                        newUserRequest.getEmailAddress());
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseMessage(false,
                        "invalid username or password");
            }
        } catch (Exception ex) {
            logger.error("{}: Error while fetching user from database with "
                    + "emailAddress: {}", Errors.DB_USER_GET,
                    newUserRequest.getEmailAddress());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage(false, "Internal error while checking "
                    + "emailAddress");
        }
        
        UserModel user = new UserModel();
        user.setEmailAddress(newUserRequest.getEmailAddress());
        user.setRoles(new LinkedList<>());
        user.getRoles().add(Roles.USER.id());
        user.setActive(true);
        user.setPassword(null);
        user.setSaltBytes(sec.generateSalt());
        user.setUserUUID(UUID.randomUUID());
        
        // Hash the user's password
        try {
            user.setPassword(sec.hashPassword(
                    newUserRequest.getPassword(),
                    user.getSaltBytes()));
        } catch (UnsupportedEncodingException ex) {
            logger.error("{}: Unsupported character encoding while generating "
                    + "password", Errors.CHAR_ENCODING);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage(false, "Internal character encoding "
                    + "error");
        }
        
        try {
            das.getUserDao().save(user);
        } catch (Exception ex) {
            logger.error("{}: Exception white creating new user in database",
                    Errors.DB_USER_CREATE);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage(false, "Internal error while creating "
                    + "new account");
        }
        return new ResponseMessage(true, user.getUserUUID().toString());
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="deleteUser">
    /**
     * Deletes a user from the database
     * @param userUUID
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(
            value="/user/{userUUID}",
            method=RequestMethod.DELETE,
            produces="application/json")
    @AllowedRoles(Roles.ADMIN)
    public ResponseMessage<Object> deleteUser(
            @PathVariable("userUUID") UUID userUUID,
            HttpServletResponse response) {
        
        try {
            das.getUserDao().delete(das.getUserDao().getByUuid(userUUID));
        } catch (Exception ex) {
            logger.error("{}: excption while deleting user account with "
                    + "UUID: {}", Errors.DB_USER_DELETE, userUUID, ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseMessage<>(false, "Internal Server Error");
        }
        return new ResponseMessage<>(true);
    }
    //</editor-fold>
    
    @ResponseBody
    @AllowedRoles(Roles.ADMIN)
    @RequestMapping(method=RequestMethod.GET,produces="application/json")
    public List<UserModel> listAllUsers(
            @RequestParam(value="maxItems", defaultValue="20") Integer maxItems,
            @RequestParam(value="startAtEmail", required=false) String startAtEmail,
            HttpServletResponse response) {
        logger.debug("got request to list all users");
        List<UserModel> emptyList = new ArrayList<>(0);
        
        // Validation
        if (startAtEmail != null && !KbdbStrings.isValidEmail(startAtEmail)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return emptyList;
        }
        
        
        
        return null;
    }
    
        
}
