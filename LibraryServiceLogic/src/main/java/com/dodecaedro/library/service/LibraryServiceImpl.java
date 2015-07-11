package com.dodecaedro.library.service;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.Fine;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.exception.BorrowNotFoundException;
import com.dodecaedro.library.repository.BorrowRepository;
import com.dodecaedro.library.repository.FineRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class LibraryServiceImpl implements LibraryService {

  @Inject
  private BorrowRepository borrowRepository;

  @Inject
  private FineRepository fineRepository;

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

  @Override
  public Borrow returnBook(User user, Book book) throws BorrowNotFoundException {
    Borrow borrow = borrowRepository.findTopByUserAndBookAndActualReturnDateIsNullOrderByBorrowDateDesc(user, book);

    if (borrow == null) {
      throw new BorrowNotFoundException("No unreturned borrow found for the user and book");
    }

    LocalDateTime nowDate = LocalDateTime.now();

    borrow.setActualReturnDate(nowDate);

    Duration fineDuration = Duration.between(borrow.getExpectedReturnDate(), nowDate);
    if (!fineDuration.isNegative() && !fineDuration.isZero()) {
      Fine fine = new Fine();
      fine.setUser(user);
      fine.setUserId(user.getUserId());
      fine.setFineEndDate(nowDate.plus(fineDuration));
      fineRepository.save(fine);
      borrow.setFine(fine);
    }

    borrowRepository.save(borrow);

    return borrow;
  }

  @Override
  public Borrow findActiveBorrow(User user, Book book) {
    return borrowRepository.findTopByUserAndBookAndActualReturnDateIsNullOrderByBorrowDateDesc(user, book);
  }
}
