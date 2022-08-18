package com.dodecaedro.library.domain.repository

import com.dodecaedro.library.domain.model.Borrow
import com.dodecaedro.library.domain.model.Fine
import com.dodecaedro.library.domain.model.User
import java.time.ZonedDateTime
import java.util.*

interface FineRepository {
	fun findTopByUserOrderByFineEndDateDesc(user: User): Fine?

	fun save(fine: Fine): Fine

	fun findActiveFinesInDate(user: User, time: ZonedDateTime): List<Fine>

	fun findByBorrow(borrow: Borrow): Fine?
}
