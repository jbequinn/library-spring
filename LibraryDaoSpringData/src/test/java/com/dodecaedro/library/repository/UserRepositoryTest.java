package com.dodecaedro.library.repository;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
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
    assertNotNull(user);
  }
}
