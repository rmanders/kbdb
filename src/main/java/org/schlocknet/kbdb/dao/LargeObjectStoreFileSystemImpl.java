package org.schlocknet.kbdb.dao;    

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.Getter;
import org.schlocknet.kbdb.config.Constants.Errors;
import static org.schlocknet.kbdb.config.Constants.MAX_DATA_OBJECT_SIZE;
import org.schlocknet.kbdb.exceptions.KbdbException;
import org.schlocknet.kbdb.util.KbdbStrings;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ryan
 * 
 * File system-based large object store
 * 
 */
public class LargeObjectStoreFileSystemImpl implements LargeObjectStore {
    
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    
    private final @Getter String basePath;
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Initialize a new File system-based object data store.
     *
     * Checks to make sure any files at teh base path are both readable and
     * writable
     *
     * @param basePath The root directory of the file store.
     *
     */
    public LargeObjectStoreFileSystemImpl(String basePath) {
        this.basePath = basePath;
        StringBuilder sb = new StringBuilder();
        sb.append("Instantiating ").append(getClass().getCanonicalName())
                .append(" with basePath: ").append(basePath);
        logger.debug(sb.toString());
        File file = new File(basePath);
        if (!file.isDirectory()) {
            throw new IllegalStateException("Base path: " + basePath
                    + " for " + getClass().getCanonicalName()
                    + " is not a directory. Must be a directory "
                    + "AND read/writable");
        }
        if (!file.canWrite() || !file.canRead()) {
            throw new IllegalStateException("Base path: " + basePath
                    + " for " + getClass().getCanonicalName()
                    + " is not readable or not writable. Must be a directory "
                    + "AND read/writable");
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getObject">
    @Override
    public byte[] getObject(String objectPath) {
        StringBuilder sb = new StringBuilder();
        final FileInputStream fis;
        try {
            sb.append(basePath).append("/").append(objectPath);
            File file = new File(sb.toString());
            if (!file.exists() && !file.isDirectory()) {
                return null;
            }
            if (file.length() > MAX_DATA_OBJECT_SIZE) {
                throw new IllegalStateException("size of data object to "
                        + "retrieve was larger than maximum allowed of "
                        + MAX_DATA_OBJECT_SIZE);
            }
            fis = new FileInputStream(sb.toString());
            return IOUtils.toByteArray(fis);
        } catch (IOException ex) {
            throw new KbdbException(Errors.LARGE_OBJECT_READ.toString(), ex);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="putObject">
    @Override
    public void putObject(String objectPath, byte[] objectData) {
        StringBuilder sb = new StringBuilder();
        if (objectData == null) {
            throw new IllegalArgumentException(
                    "objectData parameter cannot be null");
        }
        if (KbdbStrings.isBlank(basePath)) {
            throw new IllegalArgumentException(
                    "objectPath parameter cannot be null or empty");
        }
        if (objectData.length > MAX_DATA_OBJECT_SIZE) {
            throw new IllegalStateException(
                    "objectData size cannot be larger than "
                            + MAX_DATA_OBJECT_SIZE);
        }
        final File file = new File(sb.toString());
        final FileOutputStream fos;
        try {
            file.mkdirs();
            //file.createNewFile();
            sb.append(basePath).append("/").append(objectPath);
            fos = new FileOutputStream (sb.toString());
            fos.write(objectData);
            fos.close();
        } catch (IOException ex) {
            throw new KbdbException(Errors.LARGE_OBJECT_WRITE.toString(), ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deleteObject">
    @Override
    public void deleteObject(String objectPath) {
        StringBuilder sb = new StringBuilder();
        final FileInputStream fis;
        try {
            sb.append(basePath).append("/").append(objectPath);
            File file = new File(sb.toString());
            if (!file.exists() && !file.isDirectory()) {
                return;
            }
            if (file.length() > MAX_DATA_OBJECT_SIZE) {
                throw new IllegalStateException("size of data object to "
                        + "retrieve was larger than maximum allowed of "
                        + MAX_DATA_OBJECT_SIZE);
            }
            File dir = file.getParentFile();
            if (!file.delete()) {
                throw new KbdbException("Unable to delete existing object at: "
                        + file.getAbsolutePath());
            }
            // check if deleting this file makes an empty directory and if so,
            // delete the directory but fail silently if it cannot be deleted
            if (dir != null) {
                if (dir.isDirectory()) {
                    try {
                        dir.delete();
                    } catch (SecurityException ex) {
                        logger.warn("Directory {} was empty but could not be "
                                + "deleted", dir.getAbsolutePath());
                    }
                }
            }
        } catch (SecurityException ex) {
            throw new KbdbException(Errors.LARGE_OBJECT_READ.toString(), ex);
        }
    }
    //</editor-fold>    
}
