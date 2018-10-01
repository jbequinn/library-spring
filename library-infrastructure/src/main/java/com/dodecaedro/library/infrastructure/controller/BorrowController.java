package com.dodecaedro.library.infrastructure.controller;

import com.dodecaedro.library.domain.exception.ActiveFinesException;
import com.dodecaedro.library.domain.exception.BorrowMaximumLimitException;
import com.dodecaedro.library.domain.exception.ExpiredBorrowException;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.service.BorrowService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class BorrowController {
	private final BorrowService borrowService;

	public BorrowController(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	@PostMapping("/borrows/borrow")
	public @ResponseBody ResponseEntity<?> borrowBook(@RequestBody Borrow borrow)
			throws ExpiredBorrowException, ActiveFinesException, BorrowMaximumLimitException {
		Borrow createdBorrow = borrowService.borrowBook(borrow.getBook(), borrow.getUser());
		Resource<Borrow> resource = new Resource<>(createdBorrow);
		resource.add(linkTo(methodOn(BorrowController.class).borrowBook(borrow)).withSelfRel());
		return ResponseEntity.status(HttpStatus.CREATED).body(resource);
	}

	@PostMapping("/borrows/return")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void returnBook(@RequestBody Borrow borrow) {
		borrowService.returnBook(borrow.getBook(), borrow.getUser());
	}
}
