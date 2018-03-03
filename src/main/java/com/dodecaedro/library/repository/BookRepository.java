package com.dodecaedro.library.repository;

import com.dodecaedro.library.pojo.Book;
import com.dodecaedro.library.pojo.projection.BookExcerptProjection;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "books", path = "books",
  excerptProjection = BookExcerptProjection.class)
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
  @Cacheable("books")
  Optional<Book> findById(Long bookId);

  @CachePut(value = "books", key = "#p0.bookId")
  Book save(Book book);

  Optional<Book> findByIsbn(String isbn);

  List<Book> findAll();

  @CacheEvict("books")
  void delete(Book book);
}
