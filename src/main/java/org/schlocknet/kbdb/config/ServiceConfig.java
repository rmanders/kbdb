package org.schlocknet.kbdb.config;

import org.schlocknet.kbdb.service.JwtAuthenticationService;
import org.schlocknet.kbdb.service.JwtAuthenticationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ServiceConfig
{
  /**
   * Local logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfig.class);

  @Autowired
  Environment env;

  @Bean
  public JwtAuthenticationService jwtAuthenticationService() throws Exception {
    return new JwtAuthenticationServiceImpl(env.getRequiredProperty("jwt.secret"));
  }
}
