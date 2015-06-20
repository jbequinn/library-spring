package com.dodecaedro.library.service;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;

public interface LibraryService {
  Borrow borrowBook(User user, Book book);
}
