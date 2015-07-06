package com.dodecaedro.library.service;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.repository.BookRepository;
import com.dodecaedro.library.repository.BorrowRepository;
import com.dodecaedro.library.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryDaoConfiguration.class)
public class LibraryServiceIT {
  @Inject
  BookRepository bookRepository;
  @Inject
  UserRepository userRepository;
  @Inject
  BorrowRepository borrowRepository;
  @Inject
  LibraryService libraryService;

  @Test
  public void saveBorrowTest() {
    libraryService.borrowBook(userRepository.findOne(1), bookRepository.findOne(2));

    List<Borrow> borrows = borrowRepository.findAll();
    assertThat(borrows.size(), is(2));
  }
}
