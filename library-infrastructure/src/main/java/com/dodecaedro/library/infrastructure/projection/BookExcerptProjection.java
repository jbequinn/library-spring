package com.dodecaedro.library.infrastructure.projection;

import com.dodecaedro.library.domain.model.Book;
import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "bookExcerpt", types = Book.class)
public interface BookExcerptProjection {
	UUID getId();

	String getTitle();

	String getIsbn();
}
