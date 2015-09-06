package org.schlocknet.kbdb.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ryan
 * 
 * Represents a keyboard model produced by a manufacturer. 
 * 
 */
public class ManufacturerModelModel {
    
    private @Getter @Setter UUID modelUUID;
    private @Getter @Setter UUID mfgUUID;
    private @Getter @Setter String modelName;
    
}
