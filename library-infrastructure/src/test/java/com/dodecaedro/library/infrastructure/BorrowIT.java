package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.User;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.assertj.core.api.Assertions.assertThat;

@DataSet(value = "alltables.xml", strategy = SeedStrategy.CLEAN_INSERT,
		disableConstraints = true, cleanAfter = true)
public class BorrowIT extends ITBase {

	@Test
	public void createBorrow() {
		// GIVEN a valid user and book
		var user = User.builder().id(UUID.fromString("3060754f-8543-416f-b4f5-6f762c620f01")).build();
		var book = Book.builder().id(UUID.fromString("264c72cb-c43e-4160-bd92-6f5fd1b22a06")).build();

		var borrow = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.ANY)
				.body(toJson(Borrow.builder()
						.id(null)
						.user(user)
						.book(book)
						.build()))
				.when()
					.post("/borrows/borrow")
				.then().assertThat()
					.statusCode(HTTP_CREATED)
					.extract().body().as(Borrow.class);

		assertThat(borrow.getBorrowDate()).isNotNull();
		assertThat(borrow.getExpectedReturnDate()).isNotNull().isAfter(borrow.getBorrowDate());
		assertThat(borrow.getActualReturnDate()).isNull();
	}
}
