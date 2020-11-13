package com.dodecaedro.library.domain.service;

import com.dodecaedro.library.domain.LibraryProperties;
import com.dodecaedro.library.domain.exception.ActiveFinesException;
import com.dodecaedro.library.domain.exception.BorrowMaximumLimitException;
import com.dodecaedro.library.domain.exception.ExpiredBorrowException;
import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.Fine;
import com.dodecaedro.library.domain.model.User;
import com.dodecaedro.library.domain.repository.BookRepository;
import com.dodecaedro.library.domain.repository.BorrowRepository;
import com.dodecaedro.library.domain.repository.FineRepository;
import com.dodecaedro.library.domain.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class BorrowService {
	private final LibraryProperties properties;
	private final FineRepository fineRepository;
	private final BorrowRepository borrowRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	public BorrowService(LibraryProperties properties, FineRepository fineRepository,
											 BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository) {
		this.properties = requireNonNull(properties, "Properties cannot be null");
		this.fineRepository = requireNonNull(fineRepository, "Fine repository cannot be null");
		this.borrowRepository = requireNonNull(borrowRepository, "Borrow repository cannot be null");
		this.bookRepository = requireNonNull(bookRepository, "Book repository cannot be null");
		this.userRepository = requireNonNull(userRepository, "User repositoru cannot be null");
	}

	/*
	A user can borrow from the library iif:
	1. Does not have fines active
	2. Does not have unreturned items past their return date
	3. Does not exceed the maximum allowed borrowed items
	 */
	public Borrow borrowBook(Book book, User user)
		throws ActiveFinesException, ExpiredBorrowException, BorrowMaximumLimitException {
		requireNonNull(user, "user cannot be null");
		requireNonNull(user.getId(), "user id cannot be null");
		requireNonNull(book, "book cannot be null");
		requireNonNull(book.getId(), "book id cannot be null");

		var actualBook = bookRepository.findById(book.getId())
			.orElseThrow(() -> new EntityNotFoundException("Book with id: " + book.getId() + " not found."));
		var actualUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new EntityNotFoundException("User with id: " + user.getId() + " not found."));

		var nowDate = ZonedDateTime.now();

		if (!fineRepository.findActiveFinesInDate(actualUser, nowDate).isEmpty()) {
			throw new ActiveFinesException("The user has running fines");
		}
		if (borrowRepository.countExpiredBorrows(actualUser, nowDate) > 0) {
			throw new ExpiredBorrowException("cannot borrow new books because the user has expired borrows");
		}
		if (borrowRepository.countActiveBorrows(actualUser) >= properties.getMaximumBorrows()) {
			throw new BorrowMaximumLimitException("User has already reached the maximum number of simultaneous borrows");
		}

		return borrowRepository.save(new Borrow(
				UUID.randomUUID(),
				actualBook,
				actualUser,
				nowDate,
				nowDate.plusWeeks(properties.getBorrowLength()),
				null
		));
	}

	@Transactional
	public void returnBook(Book book, User user) {
		requireNonNull(user, "user cannot be null");
		requireNonNull(user.getId(), "user id cannot be null");
		requireNonNull(book, "book cannot be null");
		requireNonNull(book.getId(), "book id cannot be null");

		var actualBook = bookRepository.findById(book.getId())
			.orElseThrow(() -> new EntityNotFoundException("Book with id: " + book.getId() + " not found."));
		var actualUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new EntityNotFoundException("User with id: " + user.getId() + " not found."));

		var borrow = borrowRepository.findByBookAndUser(actualBook, actualUser)
			.orElseThrow(() -> new IllegalArgumentException("There is no active borrow for that book and user"));

		var nowDate = ZonedDateTime.now();
		borrow.setActualReturnDate(nowDate);

		if (nowDate.isAfter(borrow.getExpectedReturnDate())) {
			var fineDays = Math.max(1, ChronoUnit.DAYS.between(nowDate, borrow.getActualReturnDate()));
			fineRepository.save(Fine.builder()
				.user(actualUser)
				.fineStartDate(nowDate)
				.fineEndDate(nowDate.plusDays(fineDays))
				.build());
		}
		borrowRepository.save(borrow);
	}
}
