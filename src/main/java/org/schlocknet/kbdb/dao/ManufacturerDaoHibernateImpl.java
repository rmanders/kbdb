package org.schlocknet.kbdb.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.schlocknet.kbdb.config.Constants;
import static org.schlocknet.kbdb.config.Constants.MAX_DB_RESULT_ITEMS;
import org.schlocknet.kbdb.model.ManufacturerModel;

/**
 *
 * @author Ryan
 * 
 * Hibernate implementation of the ManufacturerDao interface
 * 
 */
public class ManufacturerDaoHibernateImpl 
extends HibernateImplBase<ManufacturerModel> implements ManufacturerDao {

    //<editor-fold defaultstate="collapsed" desc="retrieve">
    @Override
    public ManufacturerModel retrieve(UUID mfgUUID) {
        if (mfgUUID == null) {
            return null;
        }
        List<ManufacturerModel> results =  getCurrentSession()
                .createCriteria(ManufacturerModel.class, "m")
                .add(Restrictions.eq("m.mfgUUID", mfgUUID))
                .list();
        if (results == null || results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="listAll">
    @Override
    public List<ManufacturerModel> listAll(
            Integer maxItems,
            String nameContains,
            UUID startAtUUID,
            String startAtName) {
        
        Criteria c = getCurrentSession()
                .createCriteria(ManufacturerModel.class, "m")
                .addOrder(Order.asc("m.mfgName"));
        if (maxItems == null || maxItems >= MAX_DB_RESULT_ITEMS) {
            c.setMaxResults(MAX_DB_RESULT_ITEMS);
        } else if (maxItems <= 0) {
            return new LinkedList<>();
        } else {
            c.setMaxResults(maxItems);
        }
        if (nameContains != null) {
            c.add(Restrictions.like("m.mfgName", nameContains));
        }
        if (startAtUUID != null) {
            c.add(Restrictions.ge("m.mfgUUID", startAtUUID));
        }
        if (startAtName != null) {
            c.add(Restrictions.ge("m.mfgName", startAtName));
        }
        
        List<ManufacturerModel> results = c.list();
        if (results == null) {
            return new LinkedList<>();
        }
        return results;
    }
    //</editor-fold>
    
}
