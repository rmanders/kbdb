package org.schlocknet.kbdb.hibernate.types;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 *
 * @author Ryan
 * 
 * An attempt at a Hibernate custom type to make a JSON string to a java list 
 * type
 * 
 */
public class JsonListType implements UserType {
    
    private final Gson gson = new Gson();

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }

    @Override
    public Class returnedClass() {
        // Ugh
        return new ArrayList<Integer>().getClass();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        assert(x != null);
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, 
            SessionImplementor session, Object owner) 
            throws HibernateException, SQLException {
        List<Integer> result = null;
        String jsonValue = rs.getString(names[0]);
        if (jsonValue != null) {
            try {
                result = gson.fromJson(jsonValue, 
                        new TypeToken<List<Integer>>(){}.getType());
            } catch (JsonSyntaxException ex) {
                throw new HibernateException(
                        "Error decoding JSON string to list of integers",ex);
            }
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, 
            int index, SessionImplementor session) 
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, gson.toJson(value));
        }        
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        } else {
            List<Integer> orig = (List<Integer>) value;
            List<Integer> copy = new ArrayList<>(orig.size());
            for (Integer i : orig) {
                copy.add(i);
            }
            return copy;
        }
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) 
            throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) 
            throws HibernateException {
        return original;
    }
    
}
