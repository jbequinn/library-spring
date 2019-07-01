package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.repository.BookRepository;
import com.dodecaedro.library.domain.repository.BorrowRepository;
import com.dodecaedro.library.domain.repository.FineRepository;
import com.dodecaedro.library.domain.repository.UserRepository;
import com.dodecaedro.library.domain.service.BorrowService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

@Configuration
@EnableJpaRepositories(
		bootstrapMode = BootstrapMode.DEFERRED,
		basePackages = "com.dodecaedro.library.infrastructure.repository"
)
@EnableElasticsearchRepositories(
		basePackages = "com.dodecaedro.library.infrastructure.repository"
)
@EntityScan("com.dodecaedro.library.domain.model")
@EnableCaching
public class LibraryConfiguration {
	@Bean
	BorrowService borrowService(LibraryConfigurationProperties libraryConfiguration, FineRepository fineRepository,
															BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository) {
		return new BorrowService(libraryConfiguration, fineRepository, borrowRepository, bookRepository, userRepository);
	}
}
