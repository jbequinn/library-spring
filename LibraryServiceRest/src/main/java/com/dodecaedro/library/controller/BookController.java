package com.dodecaedro.library.controller;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

  @Inject
  private BookRepository bookRepository;

  @RequestMapping(method = RequestMethod.GET, produces = {"application/xml", "application/json"})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }
}
