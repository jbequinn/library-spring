package com.dodecaedro.library.infrastructure.repository;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.User;
import com.dodecaedro.library.domain.repository.BorrowRepository;
import com.dodecaedro.library.infrastructure.projection.BorrowExcerptProjection;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(
		collectionResourceRel = "borrows",
		path = "borrows",
		excerptProjection = BorrowExcerptProjection.class
)
public interface JpaBorrowRepository extends CrudRepository<Borrow, UUID>, BorrowRepository {

	@CacheEvict({"books", "users"})
	Borrow save(Borrow borrow);

	Optional<Borrow> findByBookAndUser(Book book, User user);

	@Query("""
			SELECT COUNT(b) FROM Borrow b JOIN b.user u
			WHERE u = :user
			AND b.actualReturnDate IS NULL
			AND :time > b.expectedReturnDate
			""")
	int countExpiredBorrows(@Param("user") User user, @Param("time") ZonedDateTime time);

	@Query("""
			FROM Borrow b
			JOIN FETCH b.user u
			JOIN FETCH b.book k
			WHERE b.actualReturnDate IS NULL
			AND :time > b.expectedReturnDate
			""")
	List<Borrow> findAllExpiredBorrows(@Param("time") ZonedDateTime time);

	@Query("""
			SELECT COUNT(b) FROM Borrow b JOIN b.user u
			WHERE u = :user
			AND b.actualReturnDate IS NULL
			""")
	int countActiveBorrows(User user);
}
