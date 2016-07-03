package com.dodecaedro.library.service;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.exception.ActiveFinesException;
import com.dodecaedro.library.exception.BorrowMaximumLimitException;
import com.dodecaedro.library.exception.BorrowNotFoundException;
import com.dodecaedro.library.exception.ExpiredBorrowException;

import java.util.Optional;

public interface LibraryService {
  Borrow borrowBook(User user, Book book) throws ExpiredBorrowException, ActiveFinesException, BorrowMaximumLimitException;
  Borrow returnBook(User user, Book book) throws BorrowNotFoundException;
  Optional<Borrow> findActiveBorrow(User user, Book book);
}
