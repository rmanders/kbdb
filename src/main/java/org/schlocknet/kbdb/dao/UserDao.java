/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.dao;

import java.util.UUID;
import org.schlocknet.kbdb.model.UserModel;

/**
 *
 * @author Ryan
 */
public interface UserDao {
    
    /**
     * Saves a new user record to the database. Throws an exception if the
     * user already exists.
     * 
     * @param user An instance of {@link UserModel}
     */
    public void save(UserModel user);
    
    /**
     * Updates a user's password, passwordSalt, emailAddres or roles in the 
     * database.
     * 
     * @param user An instance of {@link UserModel}
     * 
     * @return true if a user record was updates, false otherwise. 
     */
    public boolean update(UserModel user);
    
    /**
     * Deletes/removes a user record from the database based on the userUuid.
     * 
     * @param user An instance of {@link UserModel} to delete
     * 
     * @return true of a record was deleted, false otherwise.
     */
    public boolean delete(UserModel user);
    
    /**
     * Returns a user record from the database with  matching userUuid.
     * 
     * @param userUuid A {@link UUID} representing the userUuid to search for,
     * 
     * @return An instance of {@link UserModel} with the matching userUuid or
     *         null if no such user was found.
     */
    public UserModel getByUuid(UUID userUuid);
    
    /**
     * Returns a user record from the database with  matching username.
     * 
     * @param username A String representing the username to search for.
     * 
     * @return An instance of {@link UserModel} with the matching username or
     *         null if no such user was found.
     */
    public UserModel getByUsername(String username);
    
    /**
     * Returns a user record from the database with matching emailAddress.
     * 
     * @param emailAddress A String representing the emailAddress to search for.
     * 
     * @return An instance of {@link UserModel} with the matching emailAddress
     *         or null if no such user was found.
     */
    public UserModel getByEmailAddress(String emailAddress);
    
}
