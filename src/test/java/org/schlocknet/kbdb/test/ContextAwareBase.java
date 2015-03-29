/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.test;

import org.schlocknet.kbdb.config.KbdbConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author rmanders
 * 
 * Initializes a context spring for tests to use
 * 
 */
public class ContextAwareBase extends TestBase {
    
    protected final AnnotationConfigApplicationContext context;
    
    public ContextAwareBase() {
        this.context = new AnnotationConfigApplicationContext(KbdbConfigurator.class);
    }
    
}
