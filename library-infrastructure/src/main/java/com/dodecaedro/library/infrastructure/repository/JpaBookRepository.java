package com.dodecaedro.library.infrastructure.repository;

import com.dodecaedro.library.infrastructure.projection.BookExcerptProjection;
import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(
		collectionResourceRel = "books",
		path = "books",
		excerptProjection = BookExcerptProjection.class
)
public interface JpaBookRepository extends CrudRepository<Book, UUID>, BookRepository {
	@Cacheable("books")
	Optional<Book> findById(UUID bookId);

	@CachePut(value = "books", key = "#p0.bookId")
	Book save(Book book);

	Optional<Book> findByIsbn(String isbn);

	List<Book> findAll();

	@CacheEvict("books")
	void delete(Book book);
}
