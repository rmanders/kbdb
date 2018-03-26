package org.schlocknet.kbdb.security;

import java.util.Enumeration;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.schlocknet.kbdb.model.user.UserInfo;
import org.schlocknet.kbdb.service.JwtAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

/**
 * Implements a security context repository for spring-security
 */
@Component
public class AuthServiceSecurityContextRepository implements SecurityContextRepository
{
  /**
   * Local logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceSecurityContextRepository.class);

  /**
   * Regex pattern for Bearer token
   */
  private static final Pattern REGEX_BEARER_PATTERN = Pattern.compile("[Bb]earer ");

  /**
   * An instance of an authentication service that implements JSON Web Token Operations
   */
  final JwtAuthenticationService authService;

  /**
   * Default constructor, which must be passed an instance of JwtAuthenticationServiceImpl
   *
   * @param authService An instance of an authentication service that implements JSON Web Token Operations
   */
  @Autowired
  public AuthServiceSecurityContextRepository(JwtAuthenticationService authService) {
    LOGGER.info("Creating instance of AuthServiceSecurityContextRepository...");
    if (authService == null) {
      throw new IllegalStateException("Argument: JwtAuthenticationService cannot be null");
    }
    this.authService = authService;
  }

  /**
   * Creates and returns a Spring Security context from the JSON Web Token included in the request (if present)
   * @param requestResponseHolder Object containing the Http Response
   * @return an instance of a Spring security context
   */
  @Override
  public SecurityContext loadContext(final HttpRequestResponseHolder requestResponseHolder) {
    final Optional<String> token = getToken(requestResponseHolder.getRequest());

    // If the token is valid
    if (token.isPresent() && authService.isJwtValid(token.get())) {
      final Optional<UserInfo> userInfo = authService.extractUserInfo(token.get());
      if (userInfo.isPresent()) {
        final SecurityContext context = generateEmptySecurityContext();
        context.setAuthentication(new KbdbAuthenticationToken(
            userInfo.get().getUsername(),
            userInfo.get().getRoles(),
            true
        ));
      }
    }

    // Invalid or Absent token
    else
    {
      String msg = "unknown";
      if (requestResponseHolder != null && requestResponseHolder.getRequest() != null)
      {
        msg = requestResponseHolder.getRequest().getContextPath();
      }
      LOGGER.debug("Invalid or absent JSON Web Token for url: {}", msg);
    }
    return generateEmptySecurityContext();
  }

  /**
   * Doesn't do anything since JSON Web Tokens do not need to be saved
   * @param context
   * @param request
   * @param response
   */
  @Override
  public void saveContext(
      final SecurityContext context,
      final HttpServletRequest request,
      final HttpServletResponse response) {
    // Do Nothing, JSON Web Tokens are stateless
  }

  /**
   * Returns whether or not the HttpServlet Request appears to contain a JWT in the Authorization Header
   * @param request An instance of the Servlet request
   * @return true if the request contains a JWT header
   */
  @Override
  public boolean containsContext(final HttpServletRequest request) {
    final Optional<String> token = getToken(request);
    if (token.isPresent()) {
      LOGGER.debug("HttpServletRequest Contains a security context (JWT)");
    }
    return token.isPresent();
  }

  /**
   * Attempts to extract a JSON Web Token from an {@link HttpServletRequest} object
   *
   * @param request The request object to get the JSON Web Token from
   *
   * @return An optional String containing the token contents
   */
  protected Optional<String> getToken(HttpServletRequest request) {
    Optional<String> token = Optional.empty();
    if (null != request) {
      final Enumeration<String> authHeaders = request.getHeaders(HttpHeaders.AUTHORIZATION);
      while (null != authHeaders && authHeaders.hasMoreElements()) {
        final String header = authHeaders.nextElement();
        if (REGEX_BEARER_PATTERN.matcher(header).find()) {
          return Optional.ofNullable(header.substring(7));
        }
      }
    }
    return token;
  }

  /**
   * Creates and returns an empty security context
   *
   * @return
   */
  protected SecurityContext generateEmptySecurityContext() {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new KbdbAuthenticationToken());
    return context;
  }
}
