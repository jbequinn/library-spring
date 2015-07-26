package com.dodecaedro.library.configuration;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

public class LibraryServiceWebApplicationInitializer extends SpringBootServletInitializer {
  @Bean
  public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
    return new ServletRegistrationBean(dispatcherServlet, "*.json", "*.xml");
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(LibraryRestApplication.class);
  }
}
