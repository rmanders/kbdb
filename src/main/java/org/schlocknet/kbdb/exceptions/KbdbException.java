package org.schlocknet.kbdb.exceptions;

/**
 *
 * @author Ryan
 * 
 * Exception class for kbdb
 * 
 */
public class KbdbException extends RuntimeException {

    public KbdbException(String msg) {
        super(msg);
    }
    
    public KbdbException(String msg, Throwable t) {
        super(msg, t);
    }
}
