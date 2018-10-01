package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.repository.BookRepository;
import com.dodecaedro.library.domain.repository.BorrowRepository;
import com.dodecaedro.library.domain.repository.FineRepository;
import com.dodecaedro.library.domain.repository.UserRepository;
import com.dodecaedro.library.domain.service.BorrowService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryConfiguration {
	@Bean
	BorrowService borrowService(LibraryConfigurationProperties libraryConfiguration, FineRepository fineRepository,
															BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository) {
		return new BorrowService(libraryConfiguration, fineRepository, borrowRepository, bookRepository, userRepository);
	}
}
