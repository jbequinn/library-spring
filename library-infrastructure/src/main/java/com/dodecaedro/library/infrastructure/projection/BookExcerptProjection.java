package com.dodecaedro.library.infrastructure.projection;

import java.util.UUID;

public interface BookExcerptProjection {
	UUID getId();

	String getTitle();

	String getIsbn();
}
