package com.dodecaedro.library.domain.model

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
data class User(
	@Id val id: UUID? = UUID.randomUUID(),
	@Column(name = "first_name")
	var firstName: String?,
	@Column(name = "last_name")
	var lastName: String?,
	var address: String?,
	var phone: String?,
	var email: String?,
	@Column(name = "join_date")
	var joinDateTime: ZonedDateTime?,
	@OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
	var borrows: MutableList<Borrow>?,
	@OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
	var fines: MutableList<Fine>?,
)
