package org.schlocknet.kbdb.dao;

import org.schlocknet.kbdb.model.ImageMetadata;

/**
 * Interface for a DAO used to store and retrieve images
 */
public interface ImageDao {

  /**
   * Gets the binary contents of an image
   * @param imageId A unique identifier for the image
   * @return byte array with the image contents or null if no such image exists
   */
  byte [] getImage(String imageId);

  /**
   * Saves an image to the database using a unique key
   * @param imageId The unique key to save the image with
   * @param imageContents The contents of the image to save
   */
  void saveImage(String imageId, byte [] imageContents, ImageMetadata metadata);
}
