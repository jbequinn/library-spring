package com.dodecaedro.library.service;

import com.dodecaedro.library.LibraryProperties;
import com.dodecaedro.library.exception.ActiveFinesException;
import com.dodecaedro.library.exception.BorrowMaximumLimitException;
import com.dodecaedro.library.exception.ExpiredBorrowException;
import com.dodecaedro.library.pojo.Book;
import com.dodecaedro.library.pojo.Borrow;
import com.dodecaedro.library.pojo.QBorrow;
import com.dodecaedro.library.pojo.User;
import com.dodecaedro.library.repository.BorrowRepository;
import com.dodecaedro.library.repository.FineRepository;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;

@Service
@Slf4j
public class LibraryService {
  private final LibraryProperties properties;
  private final FineRepository fineRepository;
  private final BorrowRepository borrowRepository;

  public LibraryService(
      LibraryProperties properties,
      FineRepository fineRepository,
      BorrowRepository borrowRepository) {
    this.properties = properties;
    this.fineRepository = fineRepository;
    this.borrowRepository = borrowRepository;
  }

  /*
  A user can borrow from the library iif:
  1. Does not have fines active
  2. Does not have unreturned items past their return date
  3. Does not exceed the maximum allowed borrowed items
   */
  @Transactional
  public Borrow borrowBook(Book book, User user) throws Exception {
    Objects.requireNonNull(user, "user cannot be null");
    Objects.requireNonNull(user.getUserId(), "user id cannot be null");
    Objects.requireNonNull(book, "book cannot be null");
    Objects.requireNonNull(book.getBookId(), "book id cannot be null");

    if (borrowRepository.exists(sameItemStillBorrowed(user, book))) {
      throw new IllegalStateException("The user still has the same item borrowed");
    }

    ZonedDateTime nowDate = ZonedDateTime.now();

    if (!fineRepository.findActiveFinesInDate(user, nowDate).isEmpty()) {
      throw new ActiveFinesException("The user has running fines");
    }

    if (borrowRepository.exists(expiredBorrows(user, nowDate))) {
      throw new ExpiredBorrowException(
          "cannot borrow new books because the user has expired unreturned borrows");
    }

    if (properties.getMaximumBorrows() <= borrowRepository.count(activeBorrows(user))) {
      throw new BorrowMaximumLimitException(
          "User has already reached the maximum number of simultaneous borrows");
    }

    Borrow borrow =
        borrowRepository.save(
            Borrow.builder()
                .user(user)
                .book(book)
                .expectedReturnDate(nowDate.plusWeeks(properties.getBorrowLength()))
                .build());

    log.info("Created borrow: " + borrow.getId());

    return borrow;
  }

  private Predicate expiredBorrows(User user, ZonedDateTime date) {
    return QBorrow.borrow.user.eq(user)
      .and(QBorrow.borrow.actualReturnDate.lt(date));
  }

  private Predicate activeBorrows(User user) {
    return QBorrow.borrow.user.eq(user)
      .and(QBorrow.borrow.actualReturnDate.isNull());
  }

  private Predicate sameItemStillBorrowed(User user, Book book) {
    return QBorrow.borrow.user.eq(user)
      .and(QBorrow.borrow.book.eq(book))
      .and(QBorrow.borrow.actualReturnDate.isNotNull());
  }
}
