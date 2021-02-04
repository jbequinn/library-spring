package com.dodecaedro.library.infrastructure.repository;

import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.Fine;
import com.dodecaedro.library.domain.model.User;
import com.dodecaedro.library.domain.repository.FineRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaFineRepository extends CrudRepository<Fine, UUID>, FineRepository {
	Optional<Fine> findTopByUserOrderByFineEndDateDesc(User user);

	Fine save(Fine fine);

	@Query("""
			from Fine f
			where f.user = :user
			and :time BETWEEN f.fineStartDate
			and f.fineEndDate
			""")
	List<Fine> findActiveFinesInDate(@Param("user") User user, @Param("time") ZonedDateTime time);

	@Query("""
			from Fine f
			left join f.user u
			left join u.borrows b
			where b = :borrow
			""")
	Optional<Fine> findByBorrow(@Param("borrow") Borrow borrow);
}
