package org.schlocknet.kbdb.config;

/**
 *
 * @author rmanders
 * 
 * Constants for use throughout the project
 * 
 */
public class Constants {
    
    public static final String CHAR_ENCODING = "UTF-8";
    
    public static final Long JWT_EXPIRE_OFFSET = 1000 * 60 * 60 * 24L;
    
    public static final String EMAIL_FROM = "no-reply@kbdb.io";
    
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
    
    
    /**
     * Common error messages relating to kbdb
     */
    public static enum Errors {
        CHAR_ENCODING  (0x0001, "Character encoding error"),
        DB_USER_GET    (0x0002, "Failed to get User from database"),
        DB_USER_CREATE (0x0003, "Failed to create new user record in database"),
        DB_USER_UPDATE (0x0004, "Failed to update user record in database"),
        DB_USER_DELETE (0x0005, "Failed to delete user record from database"),
        EMAIL_SEND     (0x0006, "Error while trying to send an email")
        ;
        
        private final int errorNo;
        private final String errorMsg;
        private Errors(int errorNo, String errorMsg) {
            this.errorNo = errorNo;
            this.errorMsg = errorMsg;
        }
        public int getErrorNo() {return this.errorNo;}
        public String getErrorMsg() {return this.errorMsg;}
        @Override
        public String toString() {
            return String.format("[KBDB_ERROR: 0x%04X]", errorNo);
        }
    }
    
}
