package com.dodecaedro.library.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"book", "user"})
@Entity
public class Borrow {
	@Builder.Default
	@Id
	private UUID id = UUID.randomUUID();
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@Column(name = "borrow_date")
	private ZonedDateTime borrowDate;
	@Column(name = "expected_return_date")
	private ZonedDateTime expectedReturnDate;
	@Column(name = "actual_return_date")
	private ZonedDateTime actualReturnDate;
}
