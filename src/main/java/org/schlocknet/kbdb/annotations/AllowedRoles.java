/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.schlocknet.kbdb.config.Constants.Roles;

/**
 *
 * @author rmanders
 * 
 * An annotation used to limit access to certain methods based on roles
 * defined in a user's JWT. 
 * 
 * Specifically, this annotation is to be used on Spring REST Controller 
 * methods. A method handler interceptor then checks the method to be called
 * for this annotation and if exists, checks that the JWT in the request has
 * the appropriate roles.
 * 
 */

// This annotation can only be used on methods
@Target({ElementType.METHOD}) 

// Use this annotation during runtime (if this is not present, the annotation
// won't be found during runtime)
@Retention(RetentionPolicy.RUNTIME) 

public @interface AllowedRoles {
    
    public Roles[] values() default {Roles.USER};
    
}
