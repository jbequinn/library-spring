package com.dodecaedro.library.infrastructure.controller;

import com.dodecaedro.library.domain.exception.ActiveFinesException;
import com.dodecaedro.library.domain.exception.BorrowMaximumLimitException;
import com.dodecaedro.library.domain.exception.ExpiredBorrowException;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.repository.BorrowRepository;
import com.dodecaedro.library.domain.repository.UserRepository;
import com.dodecaedro.library.domain.service.BorrowService;
import com.dodecaedro.library.infrastructure.repository.JpaUserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
@RequestMapping("borrows")
@RequiredArgsConstructor
public class BorrowController {
	@NonNull private final BorrowService borrowService;
	@NonNull private final BorrowRepository borrowRepository;

	@PostMapping("borrow")
	public ResponseEntity<?> borrowBook(@RequestBody Borrow borrow)
			throws ExpiredBorrowException, ActiveFinesException, BorrowMaximumLimitException {

		var createdBorrow = borrowService.borrowBook(borrow.getBook(), borrow.getUser());

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(EntityModel.of(createdBorrow));
	}

	@PostMapping("return")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void returnBook(@RequestBody Borrow borrow) {
		borrowService.returnBook(borrow.getBook(), borrow.getUser());
	}

	@GetMapping("expired")
	public ResponseEntity<CollectionModel<BorrowModel>> getAllExpiredBorrowsAfter(
			@RequestParam("time")
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)	ZonedDateTime time) {

		var allExpiredBorrows = borrowRepository.findAllExpiredBorrows(time);

		BorrowModelAssembler assembler = new BorrowModelAssembler();
		return ResponseEntity.ok(CollectionModel.of(assembler.toCollectionModel(allExpiredBorrows)));
	}
}
