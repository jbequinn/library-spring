package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.LibraryProperties;
import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.Fine;
import com.dodecaedro.library.domain.model.User;
import com.dodecaedro.library.domain.repository.BookRepository;
import com.dodecaedro.library.domain.repository.BorrowRepository;
import com.dodecaedro.library.domain.repository.FineRepository;
import com.dodecaedro.library.domain.repository.UserRepository;
import com.dodecaedro.library.domain.service.BorrowService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.annotation.Validated;

import java.util.function.Consumer;

@Configuration
@EnableConfigurationProperties
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
	@Validated
	@ConfigurationProperties(prefix = "borrow")
	public LibraryProperties libraryProperties() {
		return new LibraryProperties();
	}

	@Bean
	BorrowService borrowService(LibraryProperties libraryProperties, FineRepository fineRepository,
															BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository) {
		return new BorrowService(libraryProperties, fineRepository, borrowRepository, bookRepository, userRepository);
	}

	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return RepositoryRestConfigurer
				.withConfig(repositoryRestConfiguration ->
						repositoryRestConfiguration.exposeIdsFor(
								Book.class, Borrow.class, Fine.class, User.class
						));
	}
}
