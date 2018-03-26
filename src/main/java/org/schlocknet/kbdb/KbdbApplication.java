package org.schlocknet.kbdb;

import static org.schlocknet.kbdb.config.Constants.FLEET_PROP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class})
public class KbdbApplication
{

  /**
   * Local logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(KbdbApplication.class);

  /**
   * Main application entry-point
   *
   * @param args Command line parameters
   */
  public static void main(String[] args) {
    initializeFleet();
    SpringApplication.run(KbdbApplication.class, args);
  }

  /**
   * Attempts to determine which fleet/environment the calling application is running in. If no information can
   * be found, it sets the environment to "dev" by default.
   */
  public static void initializeFleet() {

    // Check for and set the environment (fleet) we ar running in
    String fleet = System.getProperty(FLEET_PROP, System.getenv("FLEET"));
    if (fleet == null) {
      LOGGER.error("No [FLEET] or [spring.profiles.active] value was found. " +
          "Defaulting to spring.profiles.active=dev");
      fleet = "dev";
    }
    LOGGER.error("Executing in environment: {}", fleet);
    System.setProperty(FLEET_PROP, fleet);
  }

}
