package com.dodecaedro.library.repository;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryDaoConfiguration.class)
public class UserRepositoryTest {

  @Inject
  private UserRepository userRepository;

  @Test
  public void findAll() {
    List<User> users = userRepository.findAll();
    assertThat(users, is(not(empty())));
    assertNotNull(users);
  }

  @Test
  public void findById() {
    User user = userRepository.findOne(1);
    assertThat(user.getUserId(), is(1));
    assertThat(user.getAddress(), is("Concha Espina 1"));
    assertThat(user.getFirstName(), is("Cristiano"));
    assertThat(user.getLastName(), is("Ronaldo"));
    assertThat(user.getPhone(), is("555-555321"));
    assertThat(user.getJoinDateTime(), is(LocalDateTime.of(2014, 9, 3, 0, 0, 0)));
  }
}
