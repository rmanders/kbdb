package org.schlocknet.kbdb.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 *
 * @author Ryan
 */
public class KbdbStrings {
    
    private static final String REGEX_EMAIL = 
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+"
            + "(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    private static final Pattern emailPattern = Pattern.compile(REGEX_EMAIL);
    
    private static final SecureRandom random = new SecureRandom();

    public static String randomString() {
        return new BigInteger(130, random).toString(32);
    }
    
    public static boolean isBlank(String test) {
        return test == null || test.trim().length() == 0;
    }
    
    public static boolean isValidEmail(String test) {
        return emailPattern.matcher(test).matches();
    }
    
}
