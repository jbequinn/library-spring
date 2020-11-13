package com.dodecaedro.library.domain.service;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.util.Lists;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/* easier/faster that IT tests. test more combinations here */
@ExtendWith(MockitoExtension.class)
public class BorrowServiceTest {
	private BorrowService borrowService;

	@Mock
	private BookRepository bookRepository;
	@Mock
	private BorrowRepository borrowRepository;
	@Mock
	private FineRepository fineRepository;
	@Mock
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		borrowService = new BorrowService(
				new LibraryProperties(2, 2),
				fineRepository,
				borrowRepository, bookRepository, userRepository);
	}

	@Test
	void successfulBorrowBook() throws Exception {
		// GIVEN a book and a user
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		var user = User.builder().email("karim@benzema.com").build();

		// GIVEN that the book and the user exist in the repositories
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN that the user does not have any active fines nor expired borrows
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(Collections.emptyList());
		when(borrowRepository.countExpiredBorrows(eq(user), any()))
				.thenReturn(0);

		// GIVEN that the user has 1 active borrow (which is under the limit of the maximum)
		when(borrowRepository.countActiveBorrows(user))
				.thenReturn(1);

		// GIVEN that the repository accepts new borrows
		when(borrowRepository.save(any()))
				.thenAnswer(returnsFirstArg());

		// WHEN the user borrows the book
		var borrow = borrowService.borrowBook(book, user);

		// THEN a borrow is created
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(borrow.getUser()).isEqualTo(user);
			softly.assertThat(borrow.getBook()).isEqualTo(book);
			softly.assertThat(borrow.getBorrowDate()).isNotNull();
			softly.assertThat(borrow.getActualReturnDate()).isNull();
			softly.assertThat(borrow.getExpectedReturnDate()).isNotNull().isAfter(borrow.getBorrowDate());
		});
	}

	@Test
	void failOnActiveFines() {
		// GIVEN a book and a user
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		var user = User.builder().email("karim@benzema.com").build();

		// GIVEN that the book and the user exist in the repositories
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN that the user *does have* an active fine
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(List.of(Fine.builder().build()));

		// WHEN trying to borrow a book
		// THEN an error happens
		assertThatExceptionOfType(ActiveFinesException.class)
				.isThrownBy(() -> borrowService.borrowBook(book, user));

		// AND THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnExpiredBorrows() {
		// GIVEN a book and a user
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		var user = User.builder().email("karim@benzema.com").build();

		// GIVEN that the book and the user exist in the repositories
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN that the user does not have any fines
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(Collections.emptyList());

		// GIVEN that the user *does have* 1 expired borrow
		when(borrowRepository.countExpiredBorrows(eq(user), any()))
				.thenReturn(1);

		// WHEN trying to borrow a book
		// THEN an error happens
		assertThatExceptionOfType(ExpiredBorrowException.class)
				.isThrownBy(() -> borrowService.borrowBook(book, user));

		// AND THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnActiveBorrows() {
		// GIVEN a book and a user
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		var user = User.builder().email("karim@benzema.com").build();

		// GIVEN that the book and the user exist in the repositories
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN that the user does not have any active fines nor expired borrows
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(Collections.emptyList());
		when(borrowRepository.countExpiredBorrows(eq(user), any()))
				.thenReturn(0);

		// GIVEN that the user has already the maximum of allowed borrows
		when(borrowRepository.countActiveBorrows(user))
				.thenReturn(3);

		// WHEN trying to borrow a book
		// THEN an error happens
		assertThatExceptionOfType(BorrowMaximumLimitException.class)
				.isThrownBy(() -> borrowService.borrowBook(book, user));

		// AND THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnNullUser() {
		// GIVEN a valid book
		var validBook = new Book(UUID.randomUUID(), null, "abc-123", null);

		// WHEN trying to borrow that book by a null user
		// THEN an error happens
		assertThatNullPointerException().isThrownBy(() -> borrowService.borrowBook(validBook, null));

		// AND THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnBookWithoutId() {
		// GIVEN an invalid book: it has no id
		var invalidBook = new Book(null, null, "abc-123", null);

		// GIVEN a valid user
		User validUser = User.builder().build();

		// WHEN trying to borrow that book by a user
		// THEN an error happens
		assertThatNullPointerException().isThrownBy(() -> borrowService.borrowBook(invalidBook, validUser));

		// AND THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnNullBook() {
		// GIVEN a valid user
		User validUser = User.builder().build();

		// WHEN trying to borrow a null book by a user
		// THEN an error happens
		assertThatNullPointerException().isThrownBy(() -> borrowService.borrowBook(null, validUser));

		// AND THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnUserWithoutId() {
		// GIVEN an valid book
		var validBook = new Book(UUID.randomUUID(), null, "abc-123", null);

		// GIVEN an invalid user: he has no id
		User invalidUser = User.builder().build();
		invalidUser.setId(null);

		// WHEN trying to borrow that book by a user without id
		// THEN an error happens
		assertThatNullPointerException().isThrownBy(() -> borrowService.borrowBook(validBook, invalidUser));

		// AND THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void returnBookWithoutFine() {
		// GIVEN a book and a user
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		var user = User.builder().email("karim@benzema.com").build();

		// GIVEN that the book and the user exist in the repositories
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN that a borrow exists for that pair
		var borrow = new Borrow(
				UUID.randomUUID(),
				book,
				user,
				null,
				ZonedDateTime.now().plusDays(1),
				null
		);
		when(borrowRepository.findByBookAndUser(eq(book), eq(user)))
				.thenReturn(Optional.of(borrow));

		// GIVEN that the repository accepts new borrows
		when(borrowRepository.save(any()))
				.then(returnsFirstArg());

		// WHEN the user returns the book
		borrowService.returnBook(book, user);

		// THEN the actual return date is filled and the borrow is saved
		verify(borrowRepository).save(argThat(arg ->
				arg.getActualReturnDate() != null
		));
		// AND THEN there is no fine created
		verify(fineRepository, never()).save(any());
	}

	@Test
	void returnBookWithFine() {
		// GIVEN a book and a user
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		var user = User.builder().email("karim@benzema.com").build();

		// GIVEN that the book and the user exist in the repositories
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN a borrow exists for that pair
		var borrow = new Borrow(
				UUID.randomUUID(),
				book,
				user,
				null,
				// the book should've been returned yesterday
				ZonedDateTime.now().minusDays(1),
				null
		);
		when(borrowRepository.findByBookAndUser(eq(book), eq(user)))
				.thenReturn(Optional.of(borrow));

		// GIVEN that the repository accepts new borrows
		when(borrowRepository.save(any()))
				.then(returnsFirstArg());

		// WHEN the user returns the book
		borrowService.returnBook(book, user);

		// THEN the actual return date is filled and the borrow is saved
		verify(borrowRepository).save(argThat(arg ->
				arg.getActualReturnDate() != null
		));

		// THEN a fine is created for that user with some dates
		verify(fineRepository).save(argThat(arg ->
				arg.getUser().equals(user) &&
						arg.getFineStartDate() != null &&
						// end date is in the future
						arg.getFineEndDate().isAfter(ZonedDateTime.now())
		));
	}

	@Test
	void failOnNonExistingBorrow() {
		// GIVEN a book and a user
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		var user = User.builder().email("karim@benzema.com").build();

		// GIVEN that the book and the user exist in the repositories
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN that no borrow exists for that user/book pair
		when(borrowRepository.findByBookAndUser(eq(book), eq(user)))
				.thenReturn(Optional.empty());

		// WHEN the user tries to return the book
		// THEN an error happens
		assertThatIllegalArgumentException()
				.isThrownBy(() -> borrowService.returnBook(book, user));
	}

	@Test
	void failOnBorrowNonExistingUser() {
		// GIVEN an existing book
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);

		// GIVEN that the book exists in the repository
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN a non-existing user
		var user = User.builder().email("karim@benzema.com").build();
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.empty());

		// WHEN the non-existing user tries to borrow the book
		assertThatExceptionOfType(EntityNotFoundException.class)
				// THEN an error is thrown
				.isThrownBy(() -> borrowService.borrowBook(book, user));
	}

	@Test
	void failOnBorrowNonExistingBook() {
		// GIVEN an non-existing book
		var book = new Book(UUID.randomUUID(), null, "abc-123", null);
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.empty());

		// GIVEN an existing user
		var user = User.builder().email("karim@benzema.com").build();

		// WHEN the user tries to borrow the non-existing book
		assertThatExceptionOfType(EntityNotFoundException.class)
				// THEN an error is thrown
				.isThrownBy(() -> borrowService.borrowBook(book, user));
	}
}
