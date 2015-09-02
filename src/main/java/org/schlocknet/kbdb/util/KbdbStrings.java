package org.schlocknet.kbdb.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Ryan
 */
public class KbdbStrings {
    
    private static final SecureRandom random = new SecureRandom();

    public static String randomString() {
        return new BigInteger(130, random).toString(32);
    }
    
    public static boolean isBlank(String test) {
        return test == null || test.trim().length() == 0;
    }
    
}
