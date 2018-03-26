package org.schlocknet.kbdb.model.user;

import java.util.List;

import lombok.Value;

/**
 * Represents the minimum need user information that can be stored in a JSON Web Token
 */
@Value
public class UserInfo
{
  /** The user's unique username */
  private final String username;

  /** The roles belonging to this user */
  private final List<String> roles;
}
