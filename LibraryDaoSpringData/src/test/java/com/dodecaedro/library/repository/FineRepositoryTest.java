package com.dodecaedro.library.repository;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.Fine;
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
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryDaoConfiguration.class)
public class FineRepositoryTest {

  @Inject
  private FineRepository fineRepository;

  @Test
  public void testMostRecentFine() {
    User user = new User();
    user.setUserId(2);

    Fine fine = fineRepository.findTopByUserOrderByFineEndDateDesc(user);
    assertThat(fine.getFineEndDate(), is(LocalDateTime.of(2014, 11, 19, 18, 0, 0)));
  }

  @Test
  public void testActiveFine() {
    User user = new User();
    user.setUserId(4);

    List<Fine> fines = fineRepository.findActiveFines(user);
    assertThat(fines.size(), is(1));
  }

  @Test
  public void testNoActiveFines() {
    User user = new User();
    user.setUserId(1);

    List<Fine> fines = fineRepository.findActiveFines(user);
    assertThat(fines, is(empty()));
  }
}
