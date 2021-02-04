package com.dodecaedro.library.infrastructure.repository;

import com.dodecaedro.library.domain.model.User;
import com.dodecaedro.library.domain.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(
		collectionResourceRel = "users",
		path = "users"
)
public interface JpaUserRepository extends PagingAndSortingRepository<User, UUID>, UserRepository {
	@Cacheable("users")
	Optional<User> findById(UUID id);

	@CachePut(value = "users", key = "#p0.id")
	User save(User user);

	@Modifying
	@CacheEvict("users")
	void delete(User user);
}
