package org.schlocknet.kbdb.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
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
        .version("1.0.0")
        .termsOfServiceUrl("")
        .contact(new Contact("","",""))
        .license("")
        .licenseUrl("")
        .build();
  }
}
