package org.schlocknet.kbdb.model;

import lombok.Data;

/**
 * Contains metadata associated with a digital image (picture)
 */
@Data
public class ImageMetadata {

  /**
   * The filename (including extension) associated with the image contents
   */
  private String filename;

  /**
   * The Multipurpose Internet Mail Extension (MIME) type of the image (ex: image/png)
   */
  private String mimeType;
}
