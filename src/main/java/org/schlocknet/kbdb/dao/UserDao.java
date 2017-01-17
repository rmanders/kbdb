/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.dao;

import java.util.List;
import java.util.UUID;
import org.schlocknet.kbdb.model.User;

/**
 *
 * @author Ryan
 */
public interface UserDao {
    
    /**
     * Saves a new user record to the database. Throws an exception if the
     * user already exists.
     * 
     * @param user An instance of {@link User}
     */
    public void save(User user);
    
    /**
     * Updates a user's password, passwordSalt, emailAddres or roles in the 
     * database.
     * 
     * @param user An instance of {@link User}
     * 
     */
    public void update(User user);
    
    /**
     * Deletes/removes a user record from the database based on the userUuid.
     * 
     * @param user An instance of {@link User} to delete
     * 
     */
    public void delete(User user);
    
    /**
     * Returns a user record from the database with  matching userUuid.
     * 
     * @param userUUID A {@link UUID} representing the userUuid to search for,
     * 
     * @return An instance of {@link User} with the matching userUuid or
     *         null if no such user was found.
     */
    public User getByUuid(UUID userUUID);
        
    /**
     * Returns a user record from the database with matching emailAddress.
     * 
     * @param emailAddress A String representing the emailAddress to search for.
     * 
     * @return An instance of {@link User} with the matching emailAddress
     *         or null if no such user was found.
     */
    public User getByEmailAddress(String emailAddress);
    
    /**
     * Returns a list of all users in the database.
     * 
     * @param maxItems The maximum number of users to return
     * 
     * @param startAtEmail The email address of the user to start at in 
     * lexicographic order. If null, starts at the lowest lexicographically 
     * ordered email address.
     * 
     * @return A list of users in the database that meet the criteria specified 
     * in the parameters.
     */
    public List<User> getAllUsers(Integer maxItems, String startAtEmail);
    
}
