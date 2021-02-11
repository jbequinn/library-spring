package com.dodecaedro.library.infrastructure.projection;

import com.dodecaedro.library.domain.model.Borrow;
import org.springframework.data.rest.core.config.Projection;

import java.time.ZonedDateTime;
import java.util.UUID;

@Projection(name = "borrowSummary", types = Borrow.class)
public interface BorrowSummary {
	UUID getId();

	ZonedDateTime borrowDate();

	ZonedDateTime actualReturnDate();
}
