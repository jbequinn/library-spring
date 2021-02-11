package com.dodecaedro.library.domain.repository

import com.dodecaedro.library.domain.model.User
import java.util.*

interface UserRepository {
	fun findById(userId: UUID): Optional<User>

	fun save(user: User): User

	fun findAll(): List<User>

	fun delete(user: User)
}
