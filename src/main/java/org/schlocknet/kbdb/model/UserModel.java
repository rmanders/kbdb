/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author Ryan
 */
public class UserModel implements Serializable {
    
    private final @Getter UUID userUuid;
    private final @Getter String username;
    private final @Getter String emailAddress;
    private final @Getter byte[] password;
    private final @Getter byte[] passwordSalt;
    private final @Getter List<Integer> roles;

    public UserModel(
            UUID userUuid, 
            String username, 
            String emailAddress, 
            byte[] password, 
            byte[] passwordSalt, 
            List<Integer> roles) {
        this.userUuid = userUuid;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.passwordSalt = passwordSalt;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserModel{ username: " 
                + username
                +'}';
    }
    
}
