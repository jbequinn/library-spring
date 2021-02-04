package com.dodecaedro.library.infrastructure.projection;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.User;
import org.springframework.data.rest.core.config.Projection;

import java.time.ZonedDateTime;
import java.util.UUID;

@Projection(name = "borrowExcerpt", types = Borrow.class)
public interface BorrowExcerptProjection {
	UUID getId();

	ZonedDateTime borrowDate();

	ZonedDateTime actualReturnDate();
}
