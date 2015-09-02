package org.schlocknet.kbdb.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import static org.schlocknet.kbdb.config.Constants.CHAR_ENCODING;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ryan
 * 
 * Service class used encrypt and decrypt stuff and provide useful security 
 * related functionality.
 * 
 */
public class SecurityService {

    /** The main Algorithm to use for encryption **/
    public final static String MAIN_ALGORITHM = "HmacSHA256";
    
    public final static String MSG_DIGEST_ALGORITHM = "SHA-256";
    
    public final static int SALT_SIZE = 32;
   
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SecretKey secretKey;
    private final Mac hMacDigest;
    private final MessageDigest messageDigest;
    private final SecureRandom random;
    
    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public SecurityService(String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        logger.debug("Instantiating {}...", getClass().getCanonicalName());
        random = new SecureRandom();
        secretKey = new SecretKeySpec(Base64.getDecoder()
                .decode(key.getBytes(CHAR_ENCODING)), MAIN_ALGORITHM
        );
        hMacDigest = Mac.getInstance(MAIN_ALGORITHM);
        hMacDigest.init(secretKey);
        messageDigest = MessageDigest.getInstance(MSG_DIGEST_ALGORITHM);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="generateSalt">
    /**
     * Generates an array of random bytes for use in as a password salt.
     * @return
     */
    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        return salt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="sign">
    /**
     * Generates a signature for the specified string
     *
     * @param stringToSign
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] sign(String stringToSign)
            throws UnsupportedEncodingException {
        hMacDigest.reset();
        hMacDigest.update(stringToSign.getBytes(CHAR_ENCODING));
        return hMacDigest.doFinal();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="hashPassword">
    /**
     * Given a plaintext password and salt bytes, produces a cryptographic hash
     * of the password + salt bytes.
     *
     * @param password The plaintext password to hash
     *
     * @param saltBytes The salt bytes to add to the password before hashing
     *
     * @return The cryptographic hash of the password and salt.
     *
     * @throws UnsupportedEncodingException
     */
    public byte[] hashPassword(String password, byte[] saltBytes)
            throws UnsupportedEncodingException {
        messageDigest.reset();
        messageDigest.update(password.getBytes(CHAR_ENCODING));
        messageDigest.update(saltBytes);
        return messageDigest.digest();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="verifyPassword">
    /**
     * Given a plain text password, a salt value and a hash of the password
     * and salt, verify that the plaintext password hashed with the salt is the
     * same ad the pwHash value.
     *
     * @param password - Plaintext password to verify
     *
     * @param salt - salt bytes used to with the password
     *
     * @param pwHash - Hashed password and salt to check against
     *
     * @return true is the plaintext password is a valid match, false otherwise
     *
     * @throws UnsupportedEncodingException
     */
    public boolean verifyPassword(String password, byte[] salt, byte[] pwHash)
            throws UnsupportedEncodingException {
        return Arrays.equals(pwHash, hashPassword(password, salt));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="verifySignedString">
    /**
     * Checks that a string and its hash is a valid string that has been
     * signed by the server using the secretKey
     *
     * @param signedString - The string to check against the hash
     *
     * @param signedHash - The hash signature of the string (supposedly) signed
     * by the server.
     *
     * @return True if the string has a valid signature, false otherwise
     *
     * @throws UnsupportedEncodingException
     */
    public boolean verifySignedString(String signedString, byte[] signedHash)
            throws UnsupportedEncodingException {
        if (signedString == null || signedString.isEmpty()) {
            return false;
        }
        hMacDigest.reset();
        hMacDigest.update(signedString.getBytes(CHAR_ENCODING));
        byte[] result = hMacDigest.doFinal();
        return Arrays.equals(result, signedHash);
    }
    //</editor-fold>
    
}
