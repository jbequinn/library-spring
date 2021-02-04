package com.dodecaedro.library.infrastructure.controller;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
public class BorrowModel extends RepresentationModel<BorrowModel> {
	private final UUID id;
	private final ZonedDateTime borrowDate;
	private final ZonedDateTime expectedReturnDate;
	private final ZonedDateTime actualReturnDate;
	private final UserModel user;
	private final BookModel book;

	@Data
	@Builder
	public static class BookModel extends RepresentationModel<BookModel> {
		private final UUID id;
		private final String title;
	}

	@Data
	@Builder
	public static class UserModel extends RepresentationModel<UserModel> {
		private final UUID id;
		private final String firstName;
		private final String lastName;
	}
}
