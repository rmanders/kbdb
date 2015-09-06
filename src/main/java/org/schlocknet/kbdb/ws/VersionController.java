package org.schlocknet.kbdb.ws;

import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.schlocknet.kbdb.config.Constants.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ryan
 * 
 * Displays version information about kbdb
 * 
 */
@Controller
@RequestMapping("/version")
public class VersionController {
    
    @Autowired
    Environment env;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    //<editor-fold defaultstate="collapsed" desc="getVersion">
    /**
     * Outputs version information on kbdb
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="", method=RequestMethod.GET, produces={"text/plain"})
    public String getVersion() {
        logger.debug("Got request to print version info");
        
        StringBuilder sb = new StringBuilder();
        sb.append("Keyboard Database (kbdb) Copyright 2015 Ryan Anderson\n");
        try {
            Manifest manifest = new Manifest(getClass().getClassLoader()
                    .getResource("META-INF/MANIFEST.MF").openStream());
            Attributes attr = manifest.getMainAttributes();
            sb
                    .append("Version: ")
                    .append(attr.getValue("Implementation-Version"))
                    .append("\n")
                    .append("Build Date: ")
                    .append(attr.getValue("Kbdb-Build-Date")).append("\n")
                    ;
            
        } catch (Exception ex) {
            logger.error("{}: Exception while outputting kbdb version info",
                    Errors.CHAR_ENCODING, ex);
        }
        sb.append("Deploy environment: ")
                .append(env.getProperty("fleet", "unknown"))
                .append("\n");
        return sb.toString();
    }
    //</editor-fold>
}
