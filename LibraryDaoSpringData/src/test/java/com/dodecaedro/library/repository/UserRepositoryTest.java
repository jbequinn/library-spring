package com.dodecaedro.library.repository;

import com.dodecaedro.library.configuration.LibraryDaoConfiguration;
import com.dodecaedro.library.data.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LibraryDaoConfiguration.class)
public class UserRepositoryTest {

  @Inject
  private UserRepository userRepository;

  @Test
  public void testFindAll() {
    List<User> users = userRepository.findAll();
    assertThat(users, is(not(empty())));
    assertNotNull(users);
  }

  @Test
  public void testFindNoResults() {
    assertNull("user 99 should not exist", userRepository.findOne(99));
  }

  @Test
  public void testFindById() {
    User user = userRepository.findOne(1);
    User expectedUser = User.builder()
      .userId(1)
      .address("Concha Espina 1")
      .firstName("Cristiano")
      .lastName("Ronaldo")
      .phone("555-555321")
      .email("cris@cr7.com")
      .joinDateTime(ZonedDateTime.of(2014, 9, 3, 0, 0, 0, 0, ZoneOffset.UTC))
      .build();

    assertThat(user, is(expectedUser));
  }

  @Test
  @DirtiesContext
  public void testSaveAndLoadDates() {
    User user = User.builder()
      .firstName("James")
      .lastName("Rodriguez")
      .joinDateTime(ZonedDateTime.of(2014, 9, 3, 0, 0, 0, 0, ZoneOffset.UTC))
      .build();

    userRepository.save(user);

    User user2 = userRepository.findOne(user.getUserId());
    assertThat(user2.getJoinDateTime(), is(ZonedDateTime.of(2014, 9, 3, 0, 0, 0, 0, ZoneOffset.UTC)));
  }

  @Test
  @DirtiesContext
  public void testIdGenerated() {
    User user =  User.builder()
      .joinDateTime(ZonedDateTime.of(2014, 9, 3, 0, 0, 0, 0, ZoneOffset.UTC))
      .firstName("Marcelo")
      .build();

    userRepository.save(user);
    assertNotNull(user.getUserId());
  }

  @Test
  @DirtiesContext
  public void testDeleteUser() {
    userRepository.delete(1);

    User userDeleted = userRepository.findOne(1);
    assertNull("user 1 should not exist", userDeleted);
  }

  @Test
  @DirtiesContext
  public void testCacheChangedUser() {
    String changedEmail = "cristiano@cr7.com";

    User user = userRepository.findOne(1);
    user.setEmail(changedEmail);
    userRepository.save(user);

    User userChanged = userRepository.findOne(1);
    assertThat(userChanged.getEmail(), equalTo(changedEmail));
  }

  @Test
  @DirtiesContext
  public void testCountCacheDeleteUser() {
    int countBefore = userRepository.findAll().size();
    userRepository.delete(1);
    int countAfter = userRepository.findAll().size();

    assertThat(countAfter, is(countBefore - 1));
  }

  @Test
  public void testFinesNotEmpty() {
    User user = userRepository.getUserAndFines(2);
    assertThat(user.getFines(), is(not(empty())));
  }

  @Test
  public void testFinesEmpty() {
    User user = userRepository.getUserAndFines(1);
    assertThat(user.getFines(), is(empty()));
  }

  @Test
  public void testBorrowsNotEmpty() {
    User user = userRepository.getUserAndBorrows(1);
    assertThat(user.getBorrows(), is(not(empty())));
  }
}
