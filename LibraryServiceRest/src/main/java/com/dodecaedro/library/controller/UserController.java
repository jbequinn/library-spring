package com.dodecaedro.library.controller;

import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.Fine;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.repository.BorrowRepository;
import com.dodecaedro.library.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  @Inject
  private UserRepository userRepository;

  @Inject
  private BorrowRepository borrowRepository;

  @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<User> getCustomerByCustomerId(@PathVariable Integer userId) {
    User user = userRepository.findOne(userId);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<User> createNewUser(@RequestBody User user, UriComponentsBuilder builder) {
    userRepository.save(user);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(
      builder.path("/users/{id}")
        .buildAndExpand(user.getUserId().toString()).toUri());

    return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("id") Integer userId) {
    this.userRepository.delete(userId);
  }

  @RequestMapping(value = "/{userId}/expiredBorrows", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Borrow> getExpiredBorrows(@PathVariable Integer userId) {
    User user = new User();
    user.setUserId(userId);
    return borrowRepository.findUserExpiredBorrows(user);
  }

  @RequestMapping(value = "/{userId}/activeBorrows", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Borrow> getActiveBorrows(@PathVariable Integer userId) {
    User user = new User();
    user.setUserId(userId);
    return borrowRepository.findUserActiveBorrows(user);
  }

  @RequestMapping(value = "/{userId}/fines", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Fine> getFines(@PathVariable Integer userId) {
    return userRepository.findOne(1).getFines();
  }
}
