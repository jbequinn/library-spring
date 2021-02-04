package com.dodecaedro.library.infrastructure.repository;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(
		collectionResourceRel = "books",
		path = "books"
)
public interface JpaBookRepository extends PagingAndSortingRepository<Book, UUID>, BookRepository {
	@Cacheable("books")
	Optional<Book> findById(UUID bookId);

	@CachePut(value = "books", key = "#p0.id")
	Book save(Book book);

	List<Book> findAll();

	@Cacheable("books")
	List<Book> findAllById(Iterable<UUID> ids);

	@CacheEvict("books")
	void delete(Book book);
}
