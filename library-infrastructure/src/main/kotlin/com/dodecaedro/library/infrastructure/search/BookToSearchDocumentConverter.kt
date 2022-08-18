package com.dodecaedro.library.infrastructure.search

import com.dodecaedro.library.domain.model.Book
import org.springframework.stereotype.Component

@Component
open class BookToSearchDocumentConverter {
	fun toSearchDocument(book: Book): BookSearchDocument? = BookSearchDocument(id = book.id, title = book.title)
}
