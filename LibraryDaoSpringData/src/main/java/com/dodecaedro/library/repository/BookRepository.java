package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = Book.class, idClass = Integer.class)
public interface BookRepository {
  @Cacheable("books")
  Book findOne(Integer bookId);
  Book save(Book book);
  @Cacheable("books")
  Book findByIsbn(String isbn);
  List<Book> findAll();
}
