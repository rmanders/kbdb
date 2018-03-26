package org.schlocknet.kbdb.model;

import lombok.Value;

@Value
public class ApplicationInfo
{
  /** The name of the application */
  String applicationName;

  /** The version of the application */
  String applicationVersion;

  /** The fleet the application is deployed under */
  String fleet;

  /** The date the application was built */
  String buildDate;
}