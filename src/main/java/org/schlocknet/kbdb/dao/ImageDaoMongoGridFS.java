package org.schlocknet.kbdb.dao;

import org.schlocknet.kbdb.model.ImageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Implementation of the {@link ImageDao} using mongo gridFs to store the images
 */
public class ImageDaoMongoGridFS implements ImageDao {

  /**
   * Local logger instance
   */
   private static final Logger LOGGER = LoggerFactory.getLogger(ImageDaoMongoGridFS.class);

  /**
   * Instance of Mongo to connect to
   */
  private final MongoTemplate mongoTemplate;

  /**
   * The name of the mongo collection to use to store images
   */
  private final String imgCollectionName;

  /**
   * Default constructor
   * @param mongoTemplate Instance of Mongo to connect to
   * @param imgCollectionName The name of the mongo collection to use to store images
   */
  public ImageDaoMongoGridFS(MongoTemplate mongoTemplate, String imgCollectionName) {
    if (mongoTemplate == null) { throw new IllegalArgumentException("Argument 'mongoTemplate' cannot be null'"); }
    if (imgCollectionName == null) { throw new IllegalArgumentException("Argument 'imgCollectionName' cannot be null'"); }

    this.mongoTemplate = mongoTemplate;
    this.imgCollectionName = imgCollectionName;
    LOGGER.info("Created instance of ImageDaoMongoGridFS");
  }


  @Override
  public byte[] getImage(String imageId) {
    return new byte[0];
  }

  @Override
  public void saveImage(String imageId, byte[] imageContents, ImageMetadata metadata) {

  }
}
