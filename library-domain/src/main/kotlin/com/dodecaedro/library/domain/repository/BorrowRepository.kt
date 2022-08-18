package com.dodecaedro.library.domain.repository

import com.dodecaedro.library.domain.model.Book
import com.dodecaedro.library.domain.model.Borrow
import com.dodecaedro.library.domain.model.User
import java.time.ZonedDateTime
import java.util.*

interface BorrowRepository {
	fun save(borrow: Borrow): Borrow

	fun findByBookAndUser(book: Book, user: User): Borrow?

	fun findAll(): List<Borrow>

	fun countExpiredBorrows(user: User, nowDate: ZonedDateTime): Int

	fun findAllExpiredBorrows(time: ZonedDateTime): List<Borrow>

	fun countActiveBorrows(user: User): Int
}
