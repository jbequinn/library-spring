package com.dodecaedro.library.repository;

import com.dodecaedro.library.pojo.User;
import com.dodecaedro.library.pojo.projection.UserExcerptProjection;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
  collectionResourceRel = "users",
  path = "users",
  excerptProjection = UserExcerptProjection.class
)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
  @Cacheable("users")
  Optional<User> findById(Long userId);

  @CachePut(value = "users", key = "#p0.userId")
  User save(User user);

  List<User> findAll();

  @Modifying
  @CacheEvict("users")
  void delete(User user);
}
