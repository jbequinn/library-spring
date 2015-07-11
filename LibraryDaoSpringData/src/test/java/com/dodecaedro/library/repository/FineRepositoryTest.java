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
}
