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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryDaoConfiguration.class)
public class BorrowRepositoryTest {

  @Inject
  BookRepository bookRepository;
  @Inject
  UserRepository userRepository;
  @Inject
  BorrowRepository borrowRepository;

  @Test
  public void saveBorrowTest() {
    User user = userRepository.findOne(1);
    Book book = bookRepository.findOne(2);

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
}
