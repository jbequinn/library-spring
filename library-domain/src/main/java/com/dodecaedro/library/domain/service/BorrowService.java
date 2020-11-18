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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
public class BorrowService {
	@NonNull private final LibraryProperties properties;
	@NonNull private final FineRepository fineRepository;
	@NonNull private final BorrowRepository borrowRepository;
	@NonNull private final BookRepository bookRepository;
	@NonNull private final UserRepository userRepository;

	/*
	A user can borrow from the library iif:
	1. Does not have fines active
	2. Does not have unreturned items past their return date
	3. Does not exceed the maximum allowed borrowed items
	 */
	public Borrow borrowBook(@NonNull Book book, @NonNull User user)
		throws ActiveFinesException, ExpiredBorrowException, BorrowMaximumLimitException {
		requireNonNull(user.getId(), "user id cannot be null");
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

		return borrowRepository.save(Borrow.builder()
			.user(actualUser)
			.book(actualBook)
			.borrowDate(nowDate)
			.expectedReturnDate(nowDate.plusWeeks(properties.getBorrowLength()))
			.build());
	}

	@Transactional
	public void returnBook(@NonNull  Book book, @NonNull  User user) {
		requireNonNull(user.getId(), "user id cannot be null");
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
