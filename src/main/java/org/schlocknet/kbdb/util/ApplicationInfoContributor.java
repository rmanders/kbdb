package org.schlocknet.kbdb.util;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.schlocknet.kbdb.model.ApplicationInfo;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

/**
 * Class for contributing information to the spring boot actuator info endpoint
 */
public class ApplicationInfoContributor implements InfoContributor
{

  /** Constants defining the key names for the Info Contributor to display */
  private static final String KEY_APP_NAME = "applicationName";
  private static final String KEY_APP_VERSION = "version";
  private static final String KEY_APP_FLEET = "fleet";
  private static final String KEY_BUILD_DATE = "buildDate";

  /** Object containing application information */
  @Getter
  private final ApplicationInfo applicationInfo;

  public ApplicationInfoContributor(ApplicationInfo applicationInfo) {
    if (applicationInfo == null) {
      throw new IllegalStateException("ApplicationInfo parameter cannot be null");
    }
    this.applicationInfo = applicationInfo;
  }

  /**
   * Contributes the application info
   * @param builder
   */
  @Override
  public void contribute(final Info.Builder builder) {

    final Map<String, Object> appInfo = new HashMap<>();
    appInfo.put(KEY_APP_NAME, this.applicationInfo.getApplicationName());
    appInfo.put(KEY_APP_VERSION, this.applicationInfo.getApplicationVersion());
    appInfo.put(KEY_APP_FLEET, this.applicationInfo.getFleet());
    appInfo.put(KEY_BUILD_DATE, this.applicationInfo.getBuildDate());
    builder.withDetails(appInfo);
  }
}