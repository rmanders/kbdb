package org.schlocknet.kbdb.dao;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ryan
 * 
 * Base class for hibernate implementations of dao interfaces
 * 
 */
public class HibernateImplBase<T> {
    
    @Autowired
    SessionFactory sessionFactory;
    
    public HibernateImplBase() {
        // Do Nothing
    }
    
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    //<editor-fold defaultstate="collapsed" desc="uuidToByteArray">
    /**
     * Helper function that converts a UUID object to it's corresponding 
     * array of bytes.
     * @param uuid
     * @return 
     */
    public byte[] uuidToByteArray(UUID uuid) {
        byte[] result = new byte[0];
        if (uuid != null) {
            long hi = uuid.getMostSignificantBits();
            long lo = uuid.getLeastSignificantBits();
            result = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="save">
    /**
     * Basic CRUD operation to save a new object to the database.
     *
     * Will throw an exception if an object with the same identifier already
     * exists.
     *
     * @param o
     */
    @Transactional
    public void save(T o) {
        getCurrentSession().save(o);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="update">
    /**
     * Basic CRUD operation to update the object in the database if it exists
     * @param o 
     */
    @Transactional
    public void update(T o) {
        getCurrentSession().update(o);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="saveOrUpdate">
    /**
     * Save the object or just update it if it already exists
     * @param o 
     */
    @Transactional
    public void saveOrUpdate(T o) {
        getCurrentSession().saveOrUpdate(o);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="delete">
    /**
     * Basic CRUD operation to delete an object in the database if it exists
     * @param o 
     */
    @Transactional
    public void delete(T o) {
        getCurrentSession().delete(o);
    }
    //</editor-fold>
    
}
