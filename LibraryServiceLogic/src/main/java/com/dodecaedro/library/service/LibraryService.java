package com.dodecaedro.library.service;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.exception.BorrowNotFoundException;

public interface LibraryService {
  Borrow borrowBook(User user, Book book);
  Borrow returnBook(User user, Book book) throws BorrowNotFoundException;
  Borrow findActiveBorrow(User user, Book book);
}
