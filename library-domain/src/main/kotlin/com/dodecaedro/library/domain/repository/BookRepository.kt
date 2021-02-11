package com.dodecaedro.library.domain.repository

import com.dodecaedro.library.domain.model.Book
import java.util.*

interface BookRepository {
	fun findById(bookId: UUID): Optional<Book>

	fun save(book: Book): Book

	fun findByIsbn(isbn: String): Optional<Book>

	fun findAll(): List<Book>

	fun delete(book: Book)
}
