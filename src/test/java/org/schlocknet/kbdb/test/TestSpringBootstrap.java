/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.test;

import static junit.framework.TestCase.*;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.env.Environment;

/**
 *
 * @author rmanders
 */
@Ignore
public class TestSpringBootstrap extends ContextAwareBase {
    
    @Test
    public void testEnvironment() {
        Environment env = context.getEnvironment();
        assertNotNull(env);
    }
    
}
