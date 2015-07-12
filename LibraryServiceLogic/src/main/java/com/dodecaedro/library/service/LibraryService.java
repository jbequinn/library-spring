package com.dodecaedro.library.service;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.exception.ActiveFinesException;
import com.dodecaedro.library.exception.BorrowNotFoundException;
import com.dodecaedro.library.exception.ExpiredBorrowException;

public interface LibraryService {
  Borrow borrowBook(User user, Book book) throws ExpiredBorrowException, ActiveFinesException;
  Borrow returnBook(User user, Book book) throws BorrowNotFoundException;
  Borrow findActiveBorrow(User user, Book book);
}
