package org.schlocknet.kbdb.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.schlocknet.kbdb.config.Constants.CHAR_ENCODING;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import org.apache.commons.lang.StringUtils;
import org.schlocknet.kbdb.model.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Ryan
 * Service class used encrypt and decrypt stuff and provide useful security
 * related functionality.
 */
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService
{
  /** JWT Constants */
  public static final String JWT_ISSUER = "https://www.kbdb.io";
  public static final String JWT_AUDIENCE = "KBDB_APP_USER";
  public static final String JWT_CLAIM_USERNAME = "kbdbUsername";
  public static final String JWT_CLAIM_ROLES = "kbdbRoles";
  public static final Integer JWT_SECRET_MIN_LENGTH = 16;

  /** Encryption constants */
  public static final String MSG_DIGEST_ALGORITHM = "SHA-256";
  public static final int SALT_SIZE = 32;


  /**
   * Local logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationServiceImpl.class);

  /**
   * The algorithm used to sign a JSON Web Token
   */
  private final Algorithm algorithm;

  /**
   * Used to validate a JSON Web Token
   */
  private final JWTVerifier jwtVerifier;

  /**
   * Used for generating secure random numbers
   */
  private final SecureRandom random;

  /**
   * Used to digest a password and salt into a cryptographic hash
   */
  private final MessageDigest messageDigest;



  public JwtAuthenticationServiceImpl(String secret)
      throws UnsupportedEncodingException, NoSuchAlgorithmException
  {
    validateSecret(secret);
    this.algorithm = Algorithm.HMAC256(secret);
    this.jwtVerifier = JWT.require(this.algorithm).withIssuer(JWT_ISSUER).build();
    this.random = new SecureRandom();
    this.messageDigest = MessageDigest.getInstance(MSG_DIGEST_ALGORITHM);
  }

  /**
   * Makes sure the JWT secret meets minimum requirements for a reasonably valid JWT secret
   * @param secret The JWT Secret to validate
   */
  private void validateSecret(String secret) {
    if(StringUtils.isBlank(secret)) {
      throw new IllegalStateException("JWT Secret cannot be empty");
    } else if (secret.length() < JWT_SECRET_MIN_LENGTH) {
      throw new IllegalStateException("JWT Secret cannot be less than " + JWT_SECRET_MIN_LENGTH + " characters");
    }
  }

  /**
   * Generates an array of random bytes for use in as a password salt.
   * @return
   */
  public byte[] generateSalt()
  {
    byte[] salt = new byte[SALT_SIZE];
    random.nextBytes(salt);
    return salt;
  }

  /**
   * Given a plaintext password and salt bytes, produces a cryptographic hash
   * of the password + salt bytes.
   * @param password The plaintext password to hash
   * @param saltBytes The salt bytes to add to the password before hashing
   * @return The cryptographic hash of the password and salt.
   * @throws UnsupportedEncodingException
   */
  public byte[] hashPassword(String password, byte[] saltBytes) throws UnsupportedEncodingException
  {
    messageDigest.reset();
    messageDigest.update(password.getBytes(CHAR_ENCODING));
    messageDigest.update(saltBytes);
    return messageDigest.digest();
  }

  /**
   * Given a plain text password, a salt value and a hash of the password
   * and salt, verify that the plaintext password hashed with the salt is the
   * same ad the pwHash value.
   * @param password - Plaintext password to verify
   * @param salt - salt bytes used to with the password
   * @param pwHash - Hashed password and salt to check against
   * @return true is the plaintext password is a valid match, false otherwise
   * @throws UnsupportedEncodingException
   */
  public boolean verifyPassword(String password, byte[] salt, byte[] pwHash) throws UnsupportedEncodingException
  {
    return Arrays.equals(pwHash, hashPassword(password, salt));
  }

  @Override
  public String buildJwt(final UserInfo userInfo)
  {
    return null;
  }

  @Override
  public boolean isJwtValid(final String token)
  {
    return false;
  }

  @Override
  public Optional<UserInfo> extractUserInfo(final String token)
  {
    return Optional.empty();
  }


}
