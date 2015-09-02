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
    
    private @Getter @Setter UUID userUUID;
    private @Getter @Setter String emailAddress;
    private @Getter @Setter byte[] password;
    private @Getter @Setter byte[] saltBytes;
    private @Getter @Setter List<Integer> roles;
    private @Getter @Setter Boolean active;
    
    public UserModel() {
        roles = new LinkedList<>();
    }

    public UserModel(
            UUID userUUID, 
            String emailAddress, 
            byte[] password, 
            byte[] saltBytes, 
            List<Integer> roles,
            Boolean active) {
        this.userUUID = userUUID;
        this.emailAddress = emailAddress;
        this.password = password;
        this.saltBytes = saltBytes;
        this.roles = roles;
        this.active = active;
    }    
}
