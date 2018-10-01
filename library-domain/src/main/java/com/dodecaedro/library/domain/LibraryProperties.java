package com.dodecaedro.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryProperties {
	@Positive
	private int maximumBorrows;
	@Positive
	private int borrowLength;
}
