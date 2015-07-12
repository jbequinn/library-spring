package com.dodecaedro.library.service;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.exception.ActiveFinesException;
import com.dodecaedro.library.exception.BorrowNotFoundException;
import com.dodecaedro.library.exception.ExpiredBorrowException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.Duration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryDaoConfiguration.class)
public class LibraryServiceTest {

  @Inject
  LibraryService libraryService;

  @Test
  @DirtiesContext
  public void saveBorrowTest() throws Exception {
    User user = new User();
    user.setUserId(1);

    Book book = new Book();
    book.setBookId(2);

    libraryService.borrowBook(user, book);

    Borrow borrow = libraryService.findActiveBorrow(user, book);

    assertNotNull("This user must still have one non-returned book", borrow);
    assertThat(borrow.getBookId(), is(book.getBookId()));
    assertThat(user.getUserId(), is(user.getUserId()));
  }

  @Test(expected = BorrowNotFoundException.class)
  public void returnNonExistingBorrow() throws Exception {
    User user = new User();
    user.setUserId(1);

    Book book = new Book();
    book.setBookId(1);

    libraryService.returnBook(user, book);
  }

  @Test
  @DirtiesContext
  public void testReturnBorrow() throws Exception {
    User user = new User();
    user.setUserId(3);

    Book book = new Book();
    book.setBookId(1);

    Borrow borrow = libraryService.findActiveBorrow(user, book);
    assertNotNull("This user must still have one non-returned book", borrow);

    libraryService.returnBook(user, book);

    Borrow borrowAfter = libraryService.findActiveBorrow(user, book);
    assertNull("This user must not have any non-returned books", borrowAfter);
  }

  @Test
  @DirtiesContext
  public void testFineDate() throws Exception {
    User user = new User();
    user.setUserId(3);

    Book book = new Book();
    book.setBookId(1);

    Borrow borrow = libraryService.returnBook(user, book);
    Duration fineDuration = Duration.between(borrow.getExpectedReturnDate(), borrow.getActualReturnDate());

    assertThat(borrow.getFine().getFineEndDate(), is(borrow.getActualReturnDate().plus(fineDuration)));
  }

  @Test(expected = ActiveFinesException.class)
  public void testNoBorrowBecauseFines() throws Exception {
    User user = new User();
    user.setUserId(4);

    Book book = new Book();
    book.setBookId(1);

    libraryService.borrowBook(user, book);
  }

  @Test(expected = ExpiredBorrowException.class)
  public void testNoBorrowBecauseExpiredBorrows() throws Exception {
    User user = new User();
    user.setUserId(3);

    Book book = new Book();
    book.setBookId(1);

    libraryService.borrowBook(user, book);
  }
}
