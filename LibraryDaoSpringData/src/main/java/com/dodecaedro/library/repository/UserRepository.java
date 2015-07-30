package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface UserRepository {
  @Cacheable("users")
  User findOne(Integer userId);
  @Query("from User u join fetch u.fines f where u.userId = :userid")
  User getUserAndFines(@Param("userid")Integer userId);
  @CachePut(value = "users",  key = "#p0.userId")
  User save(User user);
  List<User> findAll();
  @CacheEvict("users")
  void delete(Integer userId);
}
