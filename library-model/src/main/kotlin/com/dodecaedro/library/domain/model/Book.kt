package com.dodecaedro.library.domain.model

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Book(
	@field:Id val id: UUID? = UUID.randomUUID(),
	var title: String?,
	var isbn: String?,
	@field:Column(name = "bought_date")
	var dateTimeBought: ZonedDateTime?,
)
