package com.dodecaedro.library.domain.repository;

import com.dodecaedro.library.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
	Optional<User> findById(UUID userId);

	User save(User user);

	List<User> findAll();

	void delete(User user);
}
