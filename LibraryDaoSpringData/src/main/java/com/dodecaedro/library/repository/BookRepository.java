package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = Book.class, idClass = Integer.class)
public interface BookRepository {
  @Cacheable("books")
  Book findOne(Integer bookId);
  @CachePut(value = "books",  key = "#p0.bookId")
  Book save(Book book);
  Book findByIsbn(String isbn);
  List<Book> findAll();
  @CacheEvict("books")
  void delete(Integer bookId);
  @Query("from User u left join u.borrows bo left join bo.book bk where bk = :book")
  List<User> findAllUsersThatBorrowed(@Param("book")Book book);
}
