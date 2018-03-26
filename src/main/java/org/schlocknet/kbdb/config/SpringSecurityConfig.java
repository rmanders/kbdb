package org.schlocknet.kbdb.config;

import org.schlocknet.kbdb.security.AuthServiceSecurityContextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter
{
  /**
   * Local logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityConfig.class);

  /** Regex pattern for urls that spring security should manage */
  private static final String REGEX_URL_PATTERN = "^\\/api/auth.+|~\\/api/profile.+";

  /**
   * Spring security context repository
   */
  @Autowired
  private AuthServiceSecurityContextRepository authServiceSecurityContextRepository;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    LOGGER.info("Configuring Spring Security...");
    httpSecurity
        .regexMatcher(REGEX_URL_PATTERN)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().anonymous()
        .and().exceptionHandling()
        .and().cors()
        .and().csrf().disable().servletApi().and().headers()
        .and().securityContext().securityContextRepository(authServiceSecurityContextRepository)
        .and().authorizeRequests()
          .antMatchers("/api/profile/**").authenticated()
          .antMatchers("/api/auth/**").permitAll();

  }
}
