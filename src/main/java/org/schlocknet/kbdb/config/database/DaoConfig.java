package org.schlocknet.kbdb.config.database;

import org.schlocknet.kbdb.dao.ImageDao;
import org.schlocknet.kbdb.dao.ImageDaoMongoGridFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Creates instances of the appropriate DAOs
 */
@Configuration
public class DaoConfig {

  /**
   * Local logger instance
   */
   private static final Logger LOGGER = LoggerFactory.getLogger(DaoConfig.class);

  private static final String DEFAULT_IMG_COLLECTION = "images";

  private final Environment env;

  private final MongoTemplate mongoTemplate;

  @Autowired
  public DaoConfig(Environment env, MongoTemplate mongoTemplate) {
    this.env = env;
    this.mongoTemplate = mongoTemplate;
  }

  @Bean
  public ImageDao imageDao() {
    final String imgCollectionName = env.getProperty("database.imgCollectionName", DEFAULT_IMG_COLLECTION);
    final ImageDao imageDao = new ImageDaoMongoGridFS(mongoTemplate, imgCollectionName);
    LOGGER.debug("Creating DAO:ImageDaoMongoGridFS with image collection name: [{}]", imgCollectionName);
    return imageDao;
  }

}
