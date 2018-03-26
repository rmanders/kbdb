package org.schlocknet.kbdb.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Implemented authentication token class for Spring Security
 */
public class KbdbAuthenticationToken implements Authentication
{

  /** The username/login of the user */
  private final String username;

  /** A list of roles this user has */
  private final List<GrantedAuthority> roles;

  /** Indicates whether or not this user ius authenticated */
  private boolean isAuthenticated = false;

  /**
   * Default constructor
   */
  public KbdbAuthenticationToken() {
    this.username = "";
    this.roles = Collections.emptyList();
    this.isAuthenticated = false;
  }

  /**
   * Constructor that initializes all attributes
   *
   * @param username The username/login of the user
   * @param roles A list of roles this user has
   * @param isAuthenticated Indicates whether or not this user ius authenticated
   */
  public KbdbAuthenticationToken(
      final String username,
      final List<String> roles,
      final boolean isAuthenticated) {
    this.username = username;
    this.isAuthenticated = isAuthenticated;
    if (null != roles && !roles.isEmpty()) {
      this.roles = roles.stream()
          .map(role -> { return new KbdbRole(role); })
          .collect(Collectors.toList());
    } else {
      this.roles = Collections.emptyList();
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  @Override
  public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
    this.isAuthenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return this.username;
  }
}
