package com.dodecaedro.library.domain.repository;

import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.Fine;
import com.dodecaedro.library.domain.model.User;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface FineRepository {
	Optional<Fine> findTopByUserOrderByFineEndDateDesc(User user);

	Fine save(Fine fine);

	List<Fine> findActiveFinesInDate(User user, ZonedDateTime time);

	Optional<Fine> findByBorrow(Borrow borrow);
}
