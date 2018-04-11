package org.schlocknet.kbdb.config.database;

import com.mongodb.Mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages="org.schlocknet.kbdb.dao.mongo")
public class MongoDbConfig
{
  /** Local logger instance */
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbConfig.class);

  /** Contains system properties and Environment variables */
  private final Environment env;

  @Autowired
  public MongoDbConfig(Environment env) {
    if (env == null) {
      throw new IllegalStateException("Argument: \"env\" cannot be null");
    }
    this.env = env;
  }

  /**
   * Creates instance of MongoClientFactoryBean
   * @return
   */
  @Bean
  public MongoClientFactoryBean mongo() {
    LOGGER.info("Creating MongoClientFactoryBean");
    final MongoClientFactoryBean mongo = new MongoClientFactoryBean();
    mongo.setHost(env.getRequiredProperty("database.host"));
    mongo.setPort(env.getProperty("database.port", Integer.class, 27017));
    return mongo;
  }

  /**
   * Instantiate and return a mongo template
   * @param mongo An instance of a mongo client
   * @return
   */
  @Bean
  public MongoTemplate mongoTemplate(Mongo mongo) {
    final String databaseName = env.getRequiredProperty("database.dbname");
    LOGGER.debug("Instantiating MongoTemplate for database: {}", databaseName);
    final MongoTemplate mongoTemplate = new MongoTemplate(mongo, databaseName);
    return mongoTemplate;
  }
}
