/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ryan
 */
public class UserModel extends JsonBase implements Serializable {
    
    private @Getter @Setter UUID userUuid;
    private @Getter @Setter String username;
    private @Getter @Setter String emailAddress;
    private @Getter @Setter byte[] password;
    private @Getter @Setter byte[] passwordSalt;
    private @Getter @Setter List<Integer> roles;
    
    public UserModel() {
        roles = new LinkedList<>();
    }

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
}
