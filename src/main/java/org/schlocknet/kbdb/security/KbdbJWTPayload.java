package org.schlocknet.kbdb.security;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.schlocknet.kbdb.model.JsonBase;

/**
 *
 * @author Ryan
 * 
 * The payload section of the JSON Web Token issued by kbdb web services
 * 
 */
public class KbdbJWTPayload extends JsonBase {
    
    private @Getter @Setter String iss = "kbdb.io";
    private @Getter @Setter Long exp = 0l;
    private @Getter @Setter String emailAddress;
    private @Getter @Setter UUID userUUID;
    private @Getter @Setter List<Integer> roles;
    
}
