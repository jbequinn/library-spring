package com.dodecaedro.library.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
