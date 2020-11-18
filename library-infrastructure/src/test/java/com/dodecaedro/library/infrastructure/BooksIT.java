package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.model.Book;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

@DataSet(
		value = "alltables.xml", strategy = SeedStrategy.CLEAN_INSERT,
		disableConstraints = true, cleanBefore = true
)
public class BooksIT extends AbstractIntegrationTest {
	@Test
	public void getAllBooks() {
		var books =
			when()
				.get("/books")
			.then().assertThat()
				.statusCode(HTTP_OK)
				.extract().jsonPath().getList("_embedded.books", Book.class);

		assertThat(books)
			.extracting(Book::getTitle)
			.hasSize(6)
			.contains("Tale of two cities");
	}

	@Test
	public void getBookById() {
		var book =
			given()
				.pathParam("id", "264c72cb-c43e-4160-bd92-6f5fd1b22a04")
			.when()
				.get("/books/{id}")
			.then().assertThat()
				.statusCode(HTTP_OK)
				.extract().body().as(Book.class);

		assertThat(book.getTitle()).isEqualTo("Once and future king");
	}

	@Test
	public void saveBook() {
		var book =
			given()
				.contentType(ContentType.JSON)
				.accept(ContentType.ANY)
				.body(toJson(Book.builder()
					.isbn("123-456-789")
					.title("Matar a un ruiseñor")
					.dateTimeBought(ZonedDateTime.parse("2014-09-01T00:00:00.0Z"))
					.build()))
			.when()
				.post("/books")
			.then().assertThat()
				.statusCode(HTTP_CREATED)
				.extract().body().as(Book.class);

		assertThat(book.getId()).isNotNull();

		var books =
			given()
				.queryParam("title", "ruiseñor")
			.when()
				.get("/books/search")
			.then().assertThat()
				.statusCode(HTTP_OK)
				.extract().jsonPath().getList("_embedded.books", Book.class);

		assertThat(books)
			.hasSize(1)
			.first()
			.extracting(Book::getTitle)
			.isEqualTo("Matar a un ruiseñor");
	}
}
