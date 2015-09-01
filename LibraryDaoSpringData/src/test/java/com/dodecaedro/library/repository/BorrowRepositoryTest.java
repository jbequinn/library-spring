package com.dodecaedro.library.repository;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.search.BorrowSpecifications;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryDaoConfiguration.class)
public class BorrowRepositoryTest {

  @Inject
  BorrowRepository borrowRepository;

  @Test
  public void testSaveBorrow() {
    User user = new User();
    user.setUserId(1);
    Book book = new Book();
    book.setBookId(2);

    Borrow borrow = new Borrow();
    borrow.setBookId(book.getBookId());
    borrow.setUserId(user.getUserId());
    borrow.setBook(book);
    borrow.setUser(user);

    ZonedDateTime nowDate = ZonedDateTime.now();
    borrow.setBorrowDate(nowDate);
    borrow.setExpectedReturnDate(nowDate.plusWeeks(2));

    borrowRepository.save(borrow);

    List<Borrow> borrows = borrowRepository.findAll();
  }

  @Test
  public void testFindNotYetReturnedBorrow() {
    User user = new User();
    user.setUserId(3);
    Book book = new Book();
    book.setBookId(1);

    Borrow borrow = borrowRepository.findTopByUserAndBookAndActualReturnDateIsNullOrderByBorrowDateDesc(user, book);

    assertThat(borrow.getBorrowDate(), is(ZonedDateTime.of(2014, 11, 3, 10, 0, 0, 0 , ZoneOffset.UTC)));
  }

  @Test
  public void testFindNoReturnedBorrows() {
    User user = new User();
    user.setUserId(1);
    Book book = new Book();
    book.setBookId(1);

    Borrow borrow = borrowRepository.findTopByUserAndBookAndActualReturnDateIsNullOrderByBorrowDateDesc(user, book);

    assertNull(borrow);
  }

  @Test
  public void testExpiredBorrows() {
    List<Borrow> expiredBorrows = borrowRepository.findAll(BorrowSpecifications.expiredBorrows(3));
    assertThat(expiredBorrows.size(), is(1));
  }

  @Test
  public void testCountExpiredBorrows() {
    Long expiredBorrows = borrowRepository.count(BorrowSpecifications.expiredBorrows(3));
    assertThat(expiredBorrows, is(1L));
  }

  @Test
  public void testExpiredBorrowsEmpty() {
    List<Borrow> expiredBorrows = borrowRepository.findAll(BorrowSpecifications.expiredBorrows(1));
    assertThat(expiredBorrows, is(empty()));
  }

  @Test
  public void testCountActiveBorrows() {
    Long openBorrows = borrowRepository.count(BorrowSpecifications.activeBorrows(5));
    assertThat(openBorrows, is(2L));
  }

  @Test
  public void testOpenBorrows() {
    List<Borrow> openBorrows = borrowRepository.findAll(BorrowSpecifications.activeBorrows(5));

    assertThat(openBorrows.size(), is(2));
  }


  @Test
  public void testNoOpenBorrows() {
    User user = new User();
    user.setUserId(1);

    Long openBorrows = borrowRepository.countByUserAndActualReturnDateIsNullOrderByBorrowDateDesc(user);

    assertThat(openBorrows, is(0L));
  }
}
