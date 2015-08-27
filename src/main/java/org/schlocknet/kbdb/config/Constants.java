package org.schlocknet.kbdb.config;

/**
 *
 * @author rmanders
 * 
 * Constants for use throughout the project
 * 
 */
public class Constants {
    
    /**
     * Custom http headers used by kbdb web services.
     */
    public static enum Headers {
        AUTH_JWT  ("kbdb-auth-token"),
        AUTH_RESP ("kbdb-auth-response");
        private Headers(String value) {this.value=value;}
        private final String value;
        public String getValue() { return value; }
    }

    /**
     * User roles for authorization
     */
    public static enum Roles {
        USER(1),
        ADMIN(2);
        private Roles(Integer roleId) {this.roleId = roleId;}
        private final Integer roleId;
        public Integer id() {return roleId;}
    }
    
}
