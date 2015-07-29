package com.dodecaedro.library.controller;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.exception.ActiveFinesException;
import com.dodecaedro.library.exception.BorrowMaximumLimitException;
import com.dodecaedro.library.exception.BorrowNotFoundException;
import com.dodecaedro.library.exception.ExpiredBorrowException;
import com.dodecaedro.library.service.LibraryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

@RestController
@RequestMapping("/borrows")
public class BorrowController {
  @Inject
  LibraryService service;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Borrow> borrowBook(@RequestBody Borrow borrow)
    throws ExpiredBorrowException, ActiveFinesException, BorrowMaximumLimitException {
    service.borrowBook(borrow.getUser(), borrow.getBook());

    HttpHeaders headers = new HttpHeaders();
    return new ResponseEntity<>(borrow, headers, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/return", method = RequestMethod.POST)
  public ResponseEntity<Borrow> returnBook(@RequestBody Borrow borrow)
    throws ExpiredBorrowException, ActiveFinesException, BorrowMaximumLimitException, BorrowNotFoundException {
    service.returnBook(borrow.getUser(), borrow.getBook());

    HttpHeaders headers = new HttpHeaders();
    return new ResponseEntity<>(borrow, headers, HttpStatus.CREATED);
  }
}
