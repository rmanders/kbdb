package org.schlocknet.kbdb.model;

import com.google.gson.Gson;
import java.io.Serializable;

/**
 *
 * @author rmanders
 * 
 * Base class for printing out objects as json strings
 * 
 */
public class JsonBase implements Serializable {
    
    @Override
    public String toString() {
        return toJson();
    }
    
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
}
