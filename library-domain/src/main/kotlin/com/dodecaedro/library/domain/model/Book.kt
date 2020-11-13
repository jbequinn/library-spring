package com.dodecaedro.library.domain.model

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Book(
		@Id val id: UUID? = UUID.randomUUID(),
		val title: String?,
		val isbn: String?,
		@Column(name = "bought_date") val dateTimeBought: ZonedDateTime?
)
