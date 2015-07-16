package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface UserRepository {
  @Cacheable("users")
  User findOne(Integer userId);
  User save(User user);
  List<User> findAll();
  void delete(Integer userId);
}
