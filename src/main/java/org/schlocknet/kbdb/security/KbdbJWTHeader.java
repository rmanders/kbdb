package org.schlocknet.kbdb.security;

import org.schlocknet.kbdb.model.JsonBase;

/**
 *
 * @author Ryan
 * 
 * JSON Web Token Header
 * 
 */
public class KbdbJWTHeader extends JsonBase {
    
    public final String typ = "JWT";
    public final String alg = "HS256";    
    
}
