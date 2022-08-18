package com.dodecaedro.library.domain.service

import com.dodecaedro.library.domain.LibraryProperties
import com.dodecaedro.library.domain.exception.ActiveFinesException
import com.dodecaedro.library.domain.exception.BorrowMaximumLimitException
import com.dodecaedro.library.domain.exception.ExpiredBorrowException
import com.dodecaedro.library.domain.model.Book
import com.dodecaedro.library.domain.model.Borrow
import com.dodecaedro.library.domain.model.Fine
import com.dodecaedro.library.domain.model.User
import com.dodecaedro.library.domain.repository.BookRepository
import com.dodecaedro.library.domain.repository.BorrowRepository
import com.dodecaedro.library.domain.repository.FineRepository
import com.dodecaedro.library.domain.repository.UserRepository
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional
import kotlin.math.max

open class BorrowService (
	open val properties: LibraryProperties,
	private val fineRepository: FineRepository,
	private val borrowRepository: BorrowRepository,
	private val bookRepository: BookRepository,
	private val userRepository: UserRepository
) {

	/*
	A user can borrow from the library iif:
	1. Does not have fines active
	2. Does not have unreturned items past their return date
	3. Does not exceed the maximum allowed borrowed items
	 */
	@Throws(ActiveFinesException::class, ExpiredBorrowException::class, BorrowMaximumLimitException::class)
	fun borrowBook(book: Book, user: User): Borrow {
		val bookId = book.id ?: throw IllegalArgumentException("book id cannot be null")
		val userId = user.id ?: throw IllegalArgumentException("user id cannot be null")
		val actualBook = bookRepository.findBookById(bookId) ?: throw EntityNotFoundException("Book with id: " + book.id + " not found.")
		val actualUser = userRepository.findUserById(userId) ?: throw EntityNotFoundException("User with id: " + user.id + " not found.")

		val nowDate = ZonedDateTime.now()
		if (fineRepository.findActiveFinesInDate(actualUser, nowDate).isNotEmpty()) {
			throw ActiveFinesException("The user has running fines")
		}
		if (borrowRepository.countExpiredBorrows(actualUser, nowDate) > 0) {
			throw ExpiredBorrowException("cannot borrow new books because the user has expired borrows")
		}
		if (borrowRepository.countActiveBorrows(actualUser) >= properties.maximumBorrows) {
			throw BorrowMaximumLimitException("User has already reached the maximum number of simultaneous borrows")
		}

		return borrowRepository.save(
			Borrow(
				id = UUID.randomUUID(),
				book = actualBook,
				user = actualUser,
				borrowDate = nowDate,
				expectedReturnDate = nowDate.plusWeeks(properties.borrowLength.toLong()),
				actualReturnDate = null
			)
		)
	}

	@Transactional
	fun returnBook(book: Book, user: User) {
		val bookId = book.id ?: throw IllegalArgumentException("book id cannot be null")
		val userId = user.id ?: throw IllegalArgumentException("user id cannot be null")
		val actualBook = bookRepository.findBookById(bookId) ?: throw EntityNotFoundException("Book with id: " + book.id + " not found.")
		val actualUser = userRepository.findUserById(userId) ?: throw EntityNotFoundException("User with id: " + user.id + " not found.")
		val borrow = borrowRepository.findByBookAndUser(actualBook, actualUser) ?: throw IllegalArgumentException("There is no active borrow for that book and user")
		val nowDate = ZonedDateTime.now()

		borrow.actualReturnDate = nowDate
		if (nowDate.isAfter(borrow.expectedReturnDate)) {
			val fineDays = max(1, ChronoUnit.DAYS.between(nowDate, borrow.actualReturnDate))
			fineRepository.save(
				Fine(
					id = UUID.randomUUID(),
					user = user,
					fineStartDate = nowDate,
					fineEndDate = nowDate.plusDays(fineDays)
				)
			)
		}
		borrowRepository.save(borrow)
	}
}
