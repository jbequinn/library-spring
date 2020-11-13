package com.dodecaedro.library.domain.model

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
data class Borrow(
		@Id val id: UUID? = UUID.randomUUID(),
		@ManyToOne
		@JoinColumn(name = "book_id")
		var book: Book?,
		@ManyToOne
		@JoinColumn(name = "user_id")
		var user: User?,
		@Column(name = "borrow_date")
		var borrowDate: ZonedDateTime?,
		@Column(name = "expected_return_date")
		var expectedReturnDate: ZonedDateTime?,
		@Column(name = "actual_return_date")
		var actualReturnDate: ZonedDateTime?,
)
