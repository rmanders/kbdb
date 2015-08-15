package org.schlocknet.kbdb.model;

import com.google.gson.Gson;

/**
 *
 * @author rmanders
 * 
 * Base class for printing out objects as json strings
 * 
 */
public class JsonBase {
    
    @Override
    public String toString() {
        return toJson();
    }
    
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
}
