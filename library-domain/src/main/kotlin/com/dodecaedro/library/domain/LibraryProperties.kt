package com.dodecaedro.library.domain

import javax.validation.constraints.Positive

data class LibraryProperties(
	@Positive var maximumBorrows: Int = 3,
	@Positive var borrowLength: Int = 2,
)
