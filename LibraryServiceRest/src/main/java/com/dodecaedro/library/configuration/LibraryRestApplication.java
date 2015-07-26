package com.dodecaedro.library.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(LibraryDaoConfiguration.class)
public class LibraryRestApplication {
  public static void main(String[] args) {
    SpringApplication.run(LibraryRestApplication.class, args);
  }
}
