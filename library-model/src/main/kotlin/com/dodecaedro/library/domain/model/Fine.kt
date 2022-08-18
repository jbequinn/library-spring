package com.dodecaedro.library.domain.model

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
data class Fine(
	@Id val id: UUID? = UUID.randomUUID(),
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	var user: User,
	@Column(name = "fine_start_date")
	var fineStartDate: ZonedDateTime,
	@Column(name = "fine_end_date")
	var fineEndDate: ZonedDateTime,
)
