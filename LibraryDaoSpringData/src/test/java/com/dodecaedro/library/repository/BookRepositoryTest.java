package com.dodecaedro.library.repository;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryDaoConfiguration.class)
public class BookRepositoryTest {

  @Inject
  private BookRepository bookRepository;

  @Test
  public void loadAllTest() {
    List<Book> books = bookRepository.findAll();
    assertThat(books, is(not(empty())));
    assertNotNull(books);
  }

  @Test
  public void loadFindByIsbnTest() {
    Book book = bookRepository.findByIsbn("1234-5678");
    assertThat(book.getBookId(), is(1));
    assertThat(book.getTitle(), is("Guerra y paz"));
    assertThat(book.getDateTimeBought(), is(LocalDateTime.of(2014,9,1, 0,0,0)));
  }
}
