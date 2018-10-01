package com.dodecaedro.library.domain.repository;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.User;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface BorrowRepository {
	Borrow save(Borrow borrow);

	Optional<Borrow> findByBookAndUser(Book book, User user);

	List<Borrow> findAll();

	int countExpiredBorrows(User user, ZonedDateTime nowDate);

	int countActiveBorrows(User user);
}
