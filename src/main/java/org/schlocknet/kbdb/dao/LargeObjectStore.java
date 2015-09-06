package org.schlocknet.kbdb.dao;

/**
 *
 * @author Ryan
 * 
 * Interface for a file-based data storage and retrieval
 * 
 */
public interface LargeObjectStore {
    
    /**
     * Retrieves file data from the given file path or null of nothing exists
     * at the specified filed path.
     * 
     * @param objectPath The path of the object to retrieve. For example:
     * file://var/www/images/image01.jpg or s3://var/www/images/image01.jpg
     * 
     * @return The object data or null or none exist at the specified path
     * 
     * @throws IllegalStateException if any of the large object sizes exceeds
     * the value of MAX_OBJECT_DATA_SIZE and KbdbException encapsulating any 
     * other general read or write exceptions.
     * 
     */
    byte[] getObject(String objectPath);
    
    /**
     * Saves data to an object at the specified fileDath
     * 
     * @param objectPath The path of the object to save data to. For example:
     * file://var/www/images/image01.jpg or s3://var/www/images/image01.jpg
     * 
     * @param objectData A byte array containing data 
     *
     * @throws IllegalStateException if any of the large object sizes exceeds
     * the value of MAX_OBJECT_DATA_SIZE and KbdbException encapsulating any 
     * other general read or write exceptions.
     */
    void putObject(String objectPath, byte[] objectData);
    
    /**
     * Deletes the object located in the specified object path
     * 
     * @param objectPath The path of the object to delete. For example:
     * file://var/www/images/image01.jpg or s3://var/www/images/image01.jpg
     * 
     * @throws IllegalStateException if any of the large object sizes exceeds
     * the value of MAX_OBJECT_DATA_SIZE and KbdbException encapsulating any 
     * other general read, write or delete exceptions.
     */
    void deleteObject(String objectPath);
   
}
