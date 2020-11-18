package com.dodecaedro.library.infrastructure.controller;

import com.dodecaedro.library.domain.exception.ActiveFinesException;
import com.dodecaedro.library.domain.exception.BorrowMaximumLimitException;
import com.dodecaedro.library.domain.exception.ExpiredBorrowException;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.service.BorrowService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
@RequestMapping("borrows")
@RequiredArgsConstructor
public class BorrowController {
	@NonNull private final BorrowService borrowService;

	@PostMapping("borrow")
	public ResponseEntity<?> borrowBook(@RequestBody Borrow borrow)
		throws ExpiredBorrowException, ActiveFinesException, BorrowMaximumLimitException {
		var createdBorrow = borrowService.borrowBook(borrow.getBook(), borrow.getUser());

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(EntityModel.of(createdBorrow)
						.add(linkTo(methodOn(BorrowController.class)
								.borrowBook(borrow))
								.withSelfRel()));
	}

	@PostMapping("return")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void returnBook(@RequestBody Borrow borrow) {
		borrowService.returnBook(borrow.getBook(), borrow.getUser());
	}
}
