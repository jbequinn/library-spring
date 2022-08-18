package com.dodecaedro.library.infrastructure

import com.dodecaedro.library.domain.LibraryProperties
import com.dodecaedro.library.domain.model.Book
import com.dodecaedro.library.domain.model.Borrow
import com.dodecaedro.library.domain.model.Fine
import com.dodecaedro.library.domain.model.User
import com.dodecaedro.library.domain.repository.BookRepository
import com.dodecaedro.library.domain.repository.BorrowRepository
import com.dodecaedro.library.domain.repository.FineRepository
import com.dodecaedro.library.domain.repository.UserRepository
import com.dodecaedro.library.domain.service.BorrowService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.config.BootstrapMode
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.validation.annotation.Validated

@Configuration(proxyBeanMethods = true)
@EnableConfigurationProperties
@EnableJpaRepositories(
	bootstrapMode = BootstrapMode.DEFERRED,
	basePackages = ["com.dodecaedro.library.infrastructure.repository"]
)
@EnableElasticsearchRepositories(basePackages = ["com.dodecaedro.library.infrastructure.repository"])
@EntityScan("com.dodecaedro.library.domain.model")
@EnableCaching
open class LibraryConfiguration {
	@Bean
	@Validated
	@ConfigurationProperties(prefix = "borrow")
	fun libraryProperties(): LibraryProperties = LibraryProperties()

	@Bean
	fun borrowService(
		libraryProperties: LibraryProperties, fineRepository: FineRepository,
		borrowRepository: BorrowRepository, bookRepository: BookRepository, userRepository: UserRepository
	): BorrowService =
		BorrowService(
			libraryProperties, fineRepository, borrowRepository,
			bookRepository, userRepository
		)

	@Bean
	fun repositoryRestConfigurer(): RepositoryRestConfigurer =
		RepositoryRestConfigurer
			.withConfig { repositoryRestConfiguration: RepositoryRestConfiguration ->
				repositoryRestConfiguration.exposeIdsFor(
					Book::class.java,
					Borrow::class.java,
					Fine::class.java,
					User::class.java
				)
			}
}
