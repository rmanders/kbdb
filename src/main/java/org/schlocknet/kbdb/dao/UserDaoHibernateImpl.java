/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schlocknet.kbdb.dao;

import java.util.List;
import java.util.UUID;
import org.hibernate.criterion.Restrictions;
import org.schlocknet.kbdb.model.UserModel;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ryan
 */
public class UserDaoHibernateImpl extends HibernateImplBase<UserModel> 
implements UserDao {

    //<editor-fold defaultstate="collapsed" desc="getByUuid">
    @Override
    @Transactional(readOnly=true)
    public UserModel getByUuid(UUID userUUID) {
        if (userUUID == null) {
            return null;
        }
        List<UserModel> results =
                getCurrentSession().createCriteria(UserModel.class, "u")
                        .add(Restrictions.eq("u.userUUID", userUUID))
                        .list();
        if (results == null || results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getByEmailAddress">
    @Override
    @Transactional(readOnly=true)
    public UserModel getByEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.trim().length() == 0) {
            return null;
        }
        List<UserModel> results =
                getCurrentSession().createCriteria(UserModel.class, "u")
                        .add(Restrictions.eq("u.emailAddress", emailAddress))
                        .list();
        if (results == null || results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
    //</editor-fold>
}
