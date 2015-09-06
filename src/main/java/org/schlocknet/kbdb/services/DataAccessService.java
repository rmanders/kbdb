package org.schlocknet.kbdb.services;

import lombok.Getter;
import org.schlocknet.kbdb.dao.LargeObjectStore;
import org.schlocknet.kbdb.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ryan
 * 
 * A class that has all of the data access objects in one place for easy access
 * 
 */
@Service
public class DataAccessService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final @Getter UserDao userDao;
    private final @Getter LargeObjectStore largeObjectStore;
    
    @Autowired
    public DataAccessService(
            UserDao userDao,
            LargeObjectStore largeObjectStore) {
        logger.debug("Instantiating {}...", getClass().getCanonicalName());
        this.userDao = userDao;
        this.largeObjectStore = largeObjectStore;
    }
    
}
