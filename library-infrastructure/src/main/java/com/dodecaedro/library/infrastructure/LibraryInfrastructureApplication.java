package com.dodecaedro.library.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

@SpringBootApplication
@EnableJpaRepositories(
		bootstrapMode = BootstrapMode.DEFERRED,
		basePackages = "com.dodecaedro.library.infrastructure.repository"
)
@EntityScan("com.dodecaedro.library.domain.model")
@EnableCaching
public class LibraryInfrastructureApplication {
	public static void main(String[] args) {
		SpringApplication.run(LibraryInfrastructureApplication.class, args);
	}
}
