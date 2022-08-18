package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.model.Borrow;
import com.dodecaedro.library.domain.model.User;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.http.ContentType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

@DataSet(
		value = "alltables.xml", strategy = SeedStrategy.CLEAN_INSERT,
		disableConstraints = true, cleanBefore = true
)
public class BorrowIT extends AbstractIntegrationTest {

	@Test
	public void createBorrow() {
		// GIVEN a valid user and book
		var user = new User(UUID.fromString("3060754f-8543-416f-b4f5-6f762c620f01"), null, null,
				null, null, null, null, null, null);
		var book = new Book(UUID.fromString("264c72cb-c43e-4160-bd92-6f5fd1b22a06"), null, null, null);

		// WHEN a new borrow is created
		var borrow =
				given()
					.contentType(ContentType.JSON)
					.accept(ContentType.ANY)
					.body(toJson(new Borrow(
							null,
							book,
							user,
							null,
							null,
							null
					)))
				.when()
					.post("/borrows/borrow")
				.then().assertThat()
					.statusCode(HTTP_CREATED)
					.extract().body().as(Borrow.class);

		// THEN the borrow has the expected dates
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(borrow.getBorrowDate()).isNotNull();
			softly.assertThat(borrow.getExpectedReturnDate()).isNotNull().isAfter(borrow.getBorrowDate());
			softly.assertThat(borrow.getActualReturnDate()).isNull();
		});
	}

	@Test
	void getAll() {
		var borrows =
				when()
					.get("/borrows")
				.then().assertThat()
					.statusCode(HTTP_OK)
					.extract().jsonPath().getList("_embedded.borrows", Borrow.class);

		assertThat(borrows).isNotEmpty();
	}

	@Test
	void findExpiredBorrows() {
		var expiredBorrows =
				given()
					.queryParam("time", "2015-01-01T00:00:00.0Z")
				.when()
					.get("/borrows/expired")
				.then().assertThat()
					.statusCode(HTTP_OK)
					.extract().jsonPath().getList("_embedded.borrowModels", Borrow.class);

		// THEN one expired borrow is found
		assertThat(expiredBorrows)
				.hasSize(1);

		SoftAssertions.assertSoftly(softly -> {
			// AND THEN this expired borrow has data about the book and the user
			softly.assertThat(expiredBorrows)
					.first()
					.extracting(Borrow::getBook)
					.extracting(Book::getTitle)
					.isEqualTo("Guerra y paz");
			softly.assertThat(expiredBorrows)
					.first()
					.extracting(Borrow::getUser)
					.extracting(User::getLastName)
					.isEqualTo("Bale");
		});
	}
}
