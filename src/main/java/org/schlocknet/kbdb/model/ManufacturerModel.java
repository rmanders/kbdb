package org.schlocknet.kbdb.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ryan
 * 
 * Describes a keyboard manufacturer
 * 
 */
public class ManufacturerModel extends JsonBase {
    
    private @Getter @Setter UUID mfgUUID;
    private @Getter @Setter String mfgName;
    private @Getter @Setter String mfgDescription;
    
}
