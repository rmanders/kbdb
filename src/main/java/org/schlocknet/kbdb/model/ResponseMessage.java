package org.schlocknet.kbdb.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ryan
 * 
 * A generic response message that can be used for multiple web service 
 * responses.

* @param <T> The type of the responseObject
 * 
 */
public class ResponseMessage<T> extends JsonBase {
    
    private @Getter @Setter boolean success;
    private @Getter @Setter String msg;
    private @Getter @Setter T responseObject;
    
    public ResponseMessage() {
        this.success = false;
        this.msg = null;
        this.responseObject = null;
    }
    
    public ResponseMessage(boolean success) {
        this.success = success;
        this.msg = null;
        this.responseObject = null;
    }
    
    public ResponseMessage(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
        this.responseObject = null;
        
    }
    
    public ResponseMessage(boolean success, String msg, T responseObject) {
        this.success = success;
        this.msg = msg;
        this.responseObject = responseObject;        
    }
}
