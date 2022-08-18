package com.dodecaedro.library.infrastructure.repository

import com.dodecaedro.library.domain.model.Book
import com.dodecaedro.library.domain.model.Borrow
import com.dodecaedro.library.domain.model.User
import com.dodecaedro.library.domain.repository.BorrowRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.time.ZonedDateTime
import java.util.*

@RepositoryRestResource(
	collectionResourceRel = "borrows",
	path = "borrows"
)
interface JpaBorrowRepository: PagingAndSortingRepository<Borrow, UUID>, BorrowRepository {
	@CacheEvict("books", "users")
	override fun save(borrow: Borrow): Borrow

	override fun findByBookAndUser(book: Book, user: User): Borrow?

	@Query("""
			SELECT COUNT(b) FROM Borrow b JOIN b.user u
			WHERE u = :user
			AND b.actualReturnDate IS NULL
			AND :time > b.expectedReturnDate
			""")
	override fun countExpiredBorrows(@Param("user") user: User, @Param("time") nowDate: ZonedDateTime): Int

	@Query("""
			FROM Borrow b
			JOIN FETCH b.user u
			JOIN FETCH b.book k
			WHERE b.actualReturnDate IS NULL
			AND :time > b.expectedReturnDate
			""")
	override fun findAllExpiredBorrows(@Param("time") time: ZonedDateTime): List<Borrow>

	@Query("""
			SELECT COUNT(b) FROM Borrow b JOIN b.user u
			WHERE u = :user
			AND b.actualReturnDate IS NULL
			""")
	override fun countActiveBorrows(@Param("user") user: User): Int
}
