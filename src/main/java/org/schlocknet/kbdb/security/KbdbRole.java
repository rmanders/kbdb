package org.schlocknet.kbdb.security;

import lombok.Value;

import org.springframework.security.core.GrantedAuthority;

@Value
public class KbdbRole implements GrantedAuthority
{
  /** The name of the role */
  private final String roleName;

  @Override
  public String getAuthority()
  {
    return roleName;
  }
}
