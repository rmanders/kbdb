package org.schlocknet.kbdb.config;

import javafx.application.Application;
import org.schlocknet.kbdb.model.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger2
public class SwaggerUIConfig {

  /** Local logger instance */
  private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerUIConfig.class);

  /** Java package path to the REST controllers */
  private static final String API_CONTROLLER_PATH = "org.schlocknet.kbdb.controller";

  /** Contains information about this application */
  private final ApplicationInfo applicationInfo;

  /**
   * Default constructor
   * @param applicationInfo
   */
  @Autowired
  public SwaggerUIConfig(ApplicationInfo applicationInfo) {
    if (applicationInfo == null) {
      throw new IllegalArgumentException("Argument: \"applicationInfo\" cannot be null");
    }
    this.applicationInfo = applicationInfo;
  }
  /**
   * Creates the Swagger-UI bean
   * @return
   */
  @Bean
  public Docket radioApi() {
    LOGGER.info("Creating Swagger UI Docket for KBDB API");
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage(API_CONTROLLER_PATH))
        .paths(PathSelectors.regex("/.*"))
        .build()
        .apiInfo(apiMetadata());
  }

  /**
   * Returns additional Metadata for the Rad.io API
   * @return
   */
  private ApiInfo apiMetadata() {
    return new ApiInfoBuilder()
        .title("KBDB API")
        .description("API Documentation for the Keyboard Database Application")
        .version(applicationInfo.getApplicationVersion())
        .termsOfServiceUrl("")
        .contact(new Contact("","",""))
        .license("")
        .licenseUrl("")
        .build();
  }

  /**
   * Tells Swagger UI to not use any thing to validate the API documentation
   * @return
   */
  @Bean
  public UiConfiguration uiConfiguration() {
    return UiConfigurationBuilder.builder()
        .validatorUrl("")
        .build();
  }
}
