package com.dodecaedro.library.infrastructure.search;

import com.dodecaedro.library.domain.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookToSearchDocumentConverter {
	public BookSearchDocument toSearchDocument(Book book) {
		return BookSearchDocument.builder()
			.id(book.getId())
			.title(book.getTitle())
			.build();
	}
}
