package com.dodecaedro.library.domain.repository

import com.dodecaedro.library.domain.model.Book
import java.util.*

interface BookRepository {

	fun findBookById(bookId: UUID): Book?

	fun save(book: Book): Book

	fun findByIsbn(isbn: String): Book?

	fun findAll(): List<Book>

	fun delete(book: Book)
}
