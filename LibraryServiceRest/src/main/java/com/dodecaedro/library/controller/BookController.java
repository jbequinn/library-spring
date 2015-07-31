package com.dodecaedro.library.controller;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.repository.BookRepository;
import com.dodecaedro.library.views.ModelViews;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/books", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
@ResponseStatus(HttpStatus.OK)

public class BookController {

  @Inject
  private BookRepository bookRepository;

  @JsonView(ModelViews.BasicBookView.class)
  @RequestMapping(value = "/{bookId}", method = RequestMethod.GET)
  public ResponseEntity<Book> getBookByBookId(@PathVariable Integer bookId) {
    Book book = bookRepository.findOne(bookId);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  @JsonView(ModelViews.BasicBookView.class)
  @RequestMapping(value = "/isbn/{isbn}", method = RequestMethod.GET)
  public ResponseEntity<Book> getBookByBookIsbn(@PathVariable String isbn) {
    Book book = bookRepository.findByIsbn(isbn);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  @JsonView(ModelViews.BasicBookView.class)
  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Book> createNewBook(@RequestBody Book book, UriComponentsBuilder builder) {
    bookRepository.save(book);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(
      builder.path("/books/{id}")
        .buildAndExpand(book.getBookId().toString()).toUri());

    return new ResponseEntity<>(book, headers, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBook(@PathVariable("id") Integer bookId) {
    this.bookRepository.delete(bookId);
  }

  @JsonView(ModelViews.BasicUserView.class)
  @RequestMapping(value = "/{bookId}/users", method = RequestMethod.GET)
  public List<User> getUsersByBook(@PathVariable Integer bookId) {
    Book book = new Book();
    book.setBookId(bookId);
    return bookRepository.findAllUsersThatBorrowed(book);
  }
}
