package com.dodecaedro.library.controller;

import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/users")
public class UserController {
  @Inject
  private UserRepository userRepository;

  @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
  public ResponseEntity<User> getCustomerByCustomerId(@PathVariable Integer userId) {
    User user = userRepository.findOne(userId);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

}
