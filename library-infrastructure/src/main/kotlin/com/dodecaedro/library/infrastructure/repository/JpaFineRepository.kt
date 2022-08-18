package com.dodecaedro.library.infrastructure.repository

import com.dodecaedro.library.domain.model.Borrow
import com.dodecaedro.library.domain.model.Fine
import com.dodecaedro.library.domain.model.User
import com.dodecaedro.library.domain.repository.FineRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.time.ZonedDateTime
import java.util.*

@RepositoryRestResource(
	collectionResourceRel = "fines",
	path = "fines"
)
interface JpaFineRepository: PagingAndSortingRepository<Fine, UUID>, FineRepository {

	override fun findTopByUserOrderByFineEndDateDesc(user: User): Fine?

	override fun save(fine: Fine): Fine

	@Query("""
			from Fine f
			where f.user = :user
			and :time BETWEEN f.fineStartDate
			and f.fineEndDate
			""")
	override fun findActiveFinesInDate(@Param("user") user: User, @Param("time") time: ZonedDateTime): List<Fine>

	@Query("""
			from Fine f
			left join f.user u
			left join u.borrows b
			where b = :borrow
			""")
	override fun findByBorrow(@Param("borrow") borrow: Borrow): Fine?
}
