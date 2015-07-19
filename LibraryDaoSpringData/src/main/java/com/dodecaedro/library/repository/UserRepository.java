package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface UserRepository {
  @Cacheable("users")
  User findOne(Integer userId);
  @CachePut("users")
  User save(User user);
  List<User> findAll();
  @CacheEvict("users")
  void delete(Integer userId);
}
