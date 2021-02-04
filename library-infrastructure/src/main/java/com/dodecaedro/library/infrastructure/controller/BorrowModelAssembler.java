package com.dodecaedro.library.infrastructure.controller;

import com.dodecaedro.library.domain.model.Borrow;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class BorrowModelAssembler extends RepresentationModelAssemblerSupport<Borrow, BorrowModel> {

	public BorrowModelAssembler() {
		super(BorrowController.class, BorrowModel.class);
	}

	@Override
	public BorrowModel toModel(Borrow borrow) {
		return BorrowModel.builder()
				.id(borrow.getId())
				.borrowDate(borrow.getBorrowDate())
				.expectedReturnDate(borrow.getExpectedReturnDate())
				.user(BorrowModel.UserModel.builder()
						.id(borrow.getUser().getId())
						.firstName(borrow.getUser().getFirstName())
						.lastName(borrow.getUser().getLastName())
						.build())
				.book(BorrowModel.BookModel.builder()
						.id(borrow.getBook().getId())
						.title(borrow.getBook().getTitle())
						.build())
				.build();
	}
}
