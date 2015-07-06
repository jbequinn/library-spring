package com.dodecaedro.library.service;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.repository.BorrowRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Service
public class LibraryServiceImpl implements LibraryService {

  @Inject
  private BorrowRepository borrowRepository;

  @Override
  public Borrow borrowBook(User user, Book book) {
    Borrow borrow = new Borrow();
    borrow.setBookId(book.getBookId());
    borrow.setUserId(user.getUserId());
    borrow.setBook(book);
    borrow.setUser(user);

    LocalDateTime nowDate = LocalDateTime.now();
    borrow.setBorrowDate(nowDate);
    borrow.setExpectedReturnDate(nowDate.plusWeeks(2));

    borrowRepository.save(borrow);

    return borrow;
  }
}
