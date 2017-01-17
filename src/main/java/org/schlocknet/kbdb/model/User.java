/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Ryan
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class User extends JsonBase implements Serializable
{

  private static final long serialVersionUID = -83573321565390646L;

  private UUID userUUID;
  private String emailAddress;
  private byte[] password;
  private byte[] saltBytes;
  private List<Integer> roles;
  private Boolean active;
  private String verificationToken;

  public User()
  {
    roles = new LinkedList<>();
  }

  public User(
    UUID userUUID,
    String emailAddress,
    byte[] password,
    byte[] saltBytes,
    List<Integer> roles,
    Boolean active,
    String verificationToken)
  {
    this.userUUID = userUUID;
    this.emailAddress = emailAddress;
    this.password = password;
    this.saltBytes = saltBytes;
    this.roles = roles;
    this.active = active;
    this.verificationToken = verificationToken;
  }
}
