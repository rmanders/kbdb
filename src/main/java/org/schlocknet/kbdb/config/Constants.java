package org.schlocknet.kbdb.config;

/**
 *
 * @author rmanders
 * 
 * Constants for use throughout the project
 * 
 */
public class Constants {
    
    public static enum Roles {
        USER(1),
        ADMIN(2);
        private Roles(Integer roleId) {this.roleId = roleId;}
        private final Integer roleId;
        public Integer id() {return roleId;}
    }
    
}
