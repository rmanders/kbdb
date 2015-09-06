/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ryan
 */
public class KeyboardModel implements Serializable {
    
    private @Getter @Setter UUID kbUUID;
    private @Getter @Setter String modelUUID;
    private @Getter @Setter String kbDescription;
    
}
