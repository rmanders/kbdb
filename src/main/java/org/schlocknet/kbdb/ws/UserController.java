package org.schlocknet.kbdb.ws;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rmanders
 */
@Controller
@RequestMapping("/users")
public class UserController {
    
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET)
    public String testOutput() {
        return "Testing...";
    }
    
}
