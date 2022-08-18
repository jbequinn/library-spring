package com.dodecaedro.library.infrastructure.repository

import com.dodecaedro.library.domain.model.User
import com.dodecaedro.library.domain.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

@RepositoryRestResource(
	collectionResourceRel = "users",
	path = "users"
)
interface JpaUserRepository: PagingAndSortingRepository<User, UUID>, UserRepository {
	@Cacheable("users")
	override fun findUserById(userId: UUID) = findByIdOrNull(userId)

	@CachePut(value = ["users"], key = "#p0.id")
	override fun save(user: User): User

	@Modifying
	@CacheEvict("users")
	override fun delete(user: User)
}
