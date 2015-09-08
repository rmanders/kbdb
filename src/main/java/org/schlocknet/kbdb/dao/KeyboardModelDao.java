package org.schlocknet.kbdb.dao;

import java.util.List;
import java.util.UUID;
import org.schlocknet.kbdb.model.ManufacturerModelModel;

/**
 *
 * @author Ryan 
 * 
 * Data access object for keyboard models
 * 
 * 
 */
public interface KeyboardModelDao {
    
    /**
     * Saves an instance of ManufacturerModelModel to the database.
     * 
     * Throws an exception if this method is called on an object that 
     * already exists in the database with the same modelUUID.
     * 
     * @param model The instance of ManufacturerModelModel to save
     */
    public void save (ManufacturerModelModel model);
    
    /**
     * 
     * Saves an instance of ManufacturerModelModel to the database or updates
     * it if it already exists.
     * 
     * @param model The instance of the ManufacturerModelModel to save or 
     * update.
     */
    public void saveOrUpdate (ManufacturerModelModel model); 
    
    
    /**
     * Updates a ManufacturerModelModel object in the database if it 
     * exists.
     * 
     * @param model The instance of the ManufacturerModelModel to update
     */
    public void update (ManufacturerModelModel model);
    
    /**
     * Deletes an instance of ManufacturerModelModel from the database if it
     * exists.
     * 
     * @param model The instance of ManufacturerModelModel to delete
     */
    public void delete (ManufacturerModelModel model);
    
    /**
     * 
     * Get a ManufacturerModelModel from the database identified by its 
     * modelUUID. 
     * 
     * Returns null if no object exists with the specified modelUUID.
     * 
     * @param modelUUID The UUID of the ManufacturerModelModel to get
     *  
     * @return An instance of ManufacturerModelModel or null
     */
    public ManufacturerModelModel retrieve(UUID modelUUID);
    
    /**
     * 
     * Gets a list of all ManufacturerModelModel objects in the database that 
     * belong to a specific manufacturer (identified by the mfgUUID parameter)
     * 
     * @param mfgUUID The UUID of the ManufacturerModel records for which to get
     * all ManufacturerModelModel records.
     * 
     * @param maxResults maxItems Max number of items to return. If null or 
     * greater than MAX_DB_RESULT_ITEMS then it is capped at that value.
     * 
     * @param startAtModelName If this value is not null, any 
     * ManufacturerModelModel instances returned in the resulting list will 
     * have a modelName lexicographically greater than or equal to this value.
     * 
     * @return A list of ManufacturerModelModel meeting the specified criteria
     */
    public List<ManufacturerModelModel> retrieveByManufacturer(
            UUID mfgUUID, 
            Integer maxResults, 
            String startAtModelName);
}
