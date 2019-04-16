package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.LibraryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "borrow")
@Validated
public class LibraryConfigurationProperties extends LibraryProperties {
}
