package com.dodecaedro.library.repository;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.LocalDateTime;
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

    LocalDateTime nowDate = LocalDateTime.now();
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

    assertThat(borrow.getBorrowDate(), is(LocalDateTime.of(2014, 11, 3, 10, 0, 0)));
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
    User user = new User();
    user.setUserId(3);

    List<Borrow> expiredBorrows = borrowRepository.findUserExpiredBorrows(user);

    assertThat(expiredBorrows.size(), is(1));
  }

  @Test
  public void testExpiredBorrowsEmpty() {
    User user = new User();
    user.setUserId(1);

    List<Borrow> expiredBorrows = borrowRepository.findUserExpiredBorrows(user);

    assertThat(expiredBorrows, is(empty()));
  }

  @Test
  public void testOpenBorrows() {
    User user = new User();
    user.setUserId(5);

    Long openBorrows = borrowRepository.countByUserAndAndActualReturnDateIsNullOrderByBorrowDateDesc(user);

    assertThat(openBorrows, is(2L));
  }

  @Test
  public void testNoOpenBorrows() {
    User user = new User();
    user.setUserId(1);

    Long openBorrows = borrowRepository.countByUserAndAndActualReturnDateIsNullOrderByBorrowDateDesc(user);

    assertThat(openBorrows, is(0L));
  }
}
