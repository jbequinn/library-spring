package com.dodecaedro.library.domain.repository;

import com.dodecaedro.library.domain.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {
	Optional<Book> findById(UUID bookId);

	Book save(Book book);

	Optional<Book> findByIsbn(String isbn);

	List<Book> findAll();

	List<Book> findAllById(Iterable<UUID> ids);

	void delete(Book book);
}
