package com.dodecaedro.library.infrastructure.controller;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class BorrowModel extends RepresentationModel<BorrowModel> {
	UUID id;
	ZonedDateTime borrowDate;
	ZonedDateTime expectedReturnDate;
	ZonedDateTime actualReturnDate;
	UserModel user;
	BookModel book;

	@Value
	@Builder
	@EqualsAndHashCode(callSuper = false)
	public static class BookModel extends RepresentationModel<BookModel> {
		UUID id;
		String title;
	}

	@Value
	@Builder
	@EqualsAndHashCode(callSuper = false)
	public static class UserModel extends RepresentationModel<UserModel> {
		UUID id;
		String firstName;
		String lastName;
	}
}
