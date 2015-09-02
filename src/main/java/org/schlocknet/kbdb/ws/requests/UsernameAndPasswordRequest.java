package org.schlocknet.kbdb.ws.requests;

import lombok.Getter;
import lombok.Setter;
import org.schlocknet.kbdb.model.JsonBase;
import org.schlocknet.kbdb.util.KbdbStrings;

/**
 *
 * @author Ryan
 * 
 * Represents a new user request
 * 
 */
public class UsernameAndPasswordRequest extends JsonBase {
    
    private @Getter @Setter String emailAddress;
    private @Getter @Setter String password;
    
    public boolean isValid() {
        // TODO: Validate email address format        
        return !(KbdbStrings.isBlank(emailAddress) 
                || KbdbStrings.isBlank(password));
    }  
}
