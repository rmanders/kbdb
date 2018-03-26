package org.schlocknet.kbdb.service;

import java.util.Optional;

import org.schlocknet.kbdb.model.user.UserInfo;

/**
 * An Authentication Service that implements JSON Web Token operations
 */
public interface JwtAuthenticationService
{
  /**
   * Creates a signed JSON Web Token from a {@link UserInfo} object
   *
   * @param userInfo An object containing basic user information
   *
   * @return A string which is a JSON Web Token signed by the server
   */
  String buildJwt(UserInfo userInfo);

  /**
   * Indicates whether or not a JSON Web Token is valid and signed by the server
   *
   * @param token The JSON Web Token to validate
   *
   * @return returns true if the JSON Web Token is valid. Returns false otherwise.
   */
  boolean isJwtValid(String token);

  /**
   * Converts a valid JSON Web Token into a UserInfo object
   *
   * @param token The JSON Web Token to extract the {@link UserInfo} object from
   *
   * @return an instance of the UserInfo object
   */
  Optional<UserInfo> extractUserInfo(String token);
}
