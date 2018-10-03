package com.dodecaedro.library.infrastructure;

import com.dodecaedro.library.domain.model.Book;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

@DataSet(value = "alltables.xml", strategy = SeedStrategy.CLEAN_INSERT,
	disableConstraints = true, cleanAfter = true)
public class BooksIT extends ITBase {
	@Test
	public void getAllBooks() {
		List<Book> books = when()
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
		Book book = given()
			.pathParam("id", "264c72cb-c43e-4160-bd92-6f5fd1b22a04")
			.when()
			.get("/books/{id}")
			.then().assertThat()
			.statusCode(HTTP_OK)
			.extract().body().as(Book.class);

		assertThat(book.getTitle()).isEqualTo("Once and future king");
	}
}
