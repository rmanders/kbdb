package org.schlocknet.kbdb.dao;

import java.util.List;
import java.util.UUID;
import org.schlocknet.kbdb.model.ManufacturerModel;

/**
 *
 * @author Ryan
 */
public interface ManufacturerDao {
    
    /**
     * Saves a new ManufacturerModel object to the database
     * 
     * @param mfgModel The ManufacturerModel object to save
     */
    public void save(ManufacturerModel mfgModel);
    
    /**
     * 
     * Saves a new ManufacturerModel object to the database or updates an 
     * existing one.
     * 
     * @param mfgModel The ManufacturerModel to save or update
     */
    public void saveOrUpdate(ManufacturerModel mfgModel);
    
    /**
     * Updates a ManufacturerModel object in the database if it exists.
     * 
     * @param mfgModel 
     */
    public void update(ManufacturerModel mfgModel);
    
    /**
     * Deletes a ManufacturerModel object from the database if one exists 
     * matching the mfgModel's modelUUID
     * 
     * @param mfgModel The ManufacturerModel object to delete
     */
    public void delete(ManufacturerModel mfgModel);
    
    /**
     * 
     * Returns a ManufacturerModel object from the database identified by its 
     * mfgUUID or null if no such objects exists.
     * 
     * @param mfgUUID The UUID of the ManufacturerModel to retrieve 
     * 
     * @return An instance of ManufacturerModel or null
     */
    public ManufacturerModel retrieve(UUID mfgUUID);
    
    /**
     * 
     * Gets a list of {@link ManufacturerModel} object from the database 
     * meeting the specified criteria.
     * 
     * @param maxItems Max number of items to return. If null or greater than 
     * MAX_DB_RESULT_ITEMS then it is capped at that value.
     * 
     * @param nameContains If not null, returns records where mfgName contains
     * this value (case sensitive)
     * 
     * @param startAtUUID If not null, returns records where the mfgUUID is 
     * lexicographically greater than or equal this value.
     * 
     * @param startAtName If not null, returns records where the mfgName is 
     * lexicographically greater then or equal to this value
     * 
     * @return A list of {@link ManufacturerModel{ objects
     */
    public List<ManufacturerModel> listAll(
            Integer maxItems, 
            String nameContains, 
            UUID startAtUUID, 
            String startAtName);
    
}
