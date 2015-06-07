/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.test;

import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.schlocknet.kbdb.config.KbdbConfigurator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Ryan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KbdbConfigurator.class)
public class TestConfigInstantiation {
    
    @Test
    public void testExistence() {
        assertTrue(true);
    }
    
}
