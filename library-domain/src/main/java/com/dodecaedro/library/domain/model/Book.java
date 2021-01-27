package com.dodecaedro.library.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
	@Builder.Default
	@Id
	private UUID id = UUID.randomUUID();
	private String title;
	private String isbn;
	@Column(name = "bought_date")
	private ZonedDateTime dateTimeBought;
}
