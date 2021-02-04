package com.dodecaedro.library.infrastructure.projection;

import com.dodecaedro.library.domain.model.Book;
import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "bookSummary", types = Book.class)
public interface BookSummary {
	UUID getId();

	String getTitle();

	String getIsbn();
}
