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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/* unit tests are cheaper to write/maintain that IT tests. test more combinations here */
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
		Mockito.reset(bookRepository, borrowRepository, fineRepository, userRepository);
		borrowService = new BorrowService(LibraryProperties.builder()
				.borrowLength(2)
				.maximumBorrows(2)
				.build(),
				fineRepository,
				borrowRepository, bookRepository, userRepository);
	}

	@Test
	void successfulBorrowBook() throws Exception {
		// GIVEN a book and a user
		var book = Book.builder().isbn("abc-123").build();
		var user = User.builder().email("karim@benzema.com").build();

		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(Collections.emptyList());
		when(borrowRepository.countExpiredBorrows(eq(user), any()))
				.thenReturn(0);
		when(borrowRepository.countActiveBorrows(user))
				.thenReturn(1);
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

		verify(fineRepository).findActiveFinesInDate(eq(user), any());
		verify(borrowRepository).countExpiredBorrows(eq(user), any());
		verify(borrowRepository).countActiveBorrows(user);
	}

	@Test
	void failOnActiveFines() {
		// GIVEN a book and a user
		var book = Book.builder().isbn("abc-123").build();
		var user = User.builder().email("karim@benzema.com").build();

		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(Lists.list(Fine.builder().build()));

		assertThatExceptionOfType(ActiveFinesException.class)
				.isThrownBy(() -> borrowService.borrowBook(book, user));

		// THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnExpiredBorrows() {
		// GIVEN a book and a user
		var book = Book.builder().isbn("abc-123").build();
		var user = User.builder().email("karim@benzema.com").build();

		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(Collections.emptyList());
		when(borrowRepository.countExpiredBorrows(eq(user), any()))
				.thenReturn(1);

		assertThatExceptionOfType(ExpiredBorrowException.class)
				.isThrownBy(() -> borrowService.borrowBook(book, user));

		// THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnActiveBorrows() {
		// GIVEN a book and a user
		var book = Book.builder().isbn("abc-123").build();
		var user = User.builder().email("karim@benzema.com").build();

		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));
		when(fineRepository.findActiveFinesInDate(eq(user), any()))
				.thenReturn(Collections.emptyList());
		when(borrowRepository.countExpiredBorrows(eq(user), any()))
				.thenReturn(0);
		when(borrowRepository.countActiveBorrows(user))
				.thenReturn(3);

		assertThatExceptionOfType(BorrowMaximumLimitException.class)
				.isThrownBy(() -> borrowService.borrowBook(book, user));

		// THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnInvalidUser() {
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThatThrownBy(() -> borrowService.borrowBook(Book.builder().build(), null))
					.isInstanceOf(NullPointerException.class);

			var book = Book.builder().build();
			book.setId(null);
			softly.assertThatThrownBy(() -> borrowService.borrowBook(book, null))
					.isInstanceOf(NullPointerException.class);
		});

		// THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void failOnInvalidBook() {
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThatThrownBy(() -> borrowService.borrowBook(null, User.builder().build()))
					.isInstanceOf(NullPointerException.class);

			var user = User.builder().build();
			user.setId(null);
			softly.assertThatThrownBy(() -> borrowService.borrowBook(null, user))
					.isInstanceOf(NullPointerException.class);
		});

		// THEN no borrow is created
		verify(borrowRepository, never()).save(any());
	}

	@Test
	void returnBookWithoutFine() {
		// GIVEN a book and a user
		var book = Book.builder().isbn("abc-123").build();
		var user = User.builder().email("karim@benzema.com").build();
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN a borrow exists for that pair
		var borrow = Borrow.builder()
				.book(book)
				.user(user)
				.expectedReturnDate(ZonedDateTime.now().plusDays(1))
				.build();

		when(borrowRepository.findByBookAndUser(eq(book), eq(user)))
				.thenReturn(Optional.of(borrow));
		when(borrowRepository.save(any()))
				.then(returnsFirstArg());

		// WHEN the user returns the book
		borrowService.returnBook(book, user);

		// THEN the actual return date is filled and the borrow is saved
		verify(borrowRepository).save(argThat(arg ->
				arg.getActualReturnDate() != null
		));
		// THEN there is no fine created
		verify(fineRepository, never()).save(any());
	}

	@Test
	void returnBookWithFine() {
		// GIVEN a book and a user
		var book = Book.builder().isbn("abc-123").build();
		var user = User.builder().email("karim@benzema.com").build();
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		// GIVEN a borrow exists for that pair
		var borrow = Borrow.builder()
				.book(book)
				.user(user)
				// the book should've been returned yesterday
				.expectedReturnDate(ZonedDateTime.now().minusDays(1))
				.build();

		when(borrowRepository.findByBookAndUser(eq(book), eq(user)))
				.thenReturn(Optional.of(borrow));
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
		var book = Book.builder().isbn("abc-123").build();
		var user = User.builder().email("karim@benzema.com").build();
		when(userRepository.findById(eq(user.getId())))
				.thenReturn(Optional.of(user));
		when(bookRepository.findById(eq(book.getId())))
				.thenReturn(Optional.of(book));

		when(borrowRepository.findByBookAndUser(eq(book), eq(user)))
				.thenReturn(Optional.empty());

		assertThatIllegalArgumentException()
				.isThrownBy(() -> borrowService.returnBook(book, user));
	}

	@Test
	void failOnBorrowNonExistingUser() {
		// GIVEN an existing book
		var book = Book.builder().isbn("abc-123").build();
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
		var book = Book.builder().isbn("abc-123").build();
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
