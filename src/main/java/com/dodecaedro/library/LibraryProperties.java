package com.dodecaedro.library;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "library")
public class LibraryProperties {
  private int maximumBorrows;
  private int borrowLength;
}
