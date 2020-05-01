package com.dodecaedro.library.infrastructure;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.dodecaedro.library.domain.model.User;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

@DataSet(
		value = "alltables.xml", strategy = SeedStrategy.CLEAN_INSERT,
		disableConstraints = true, cleanAfter = true
)
public class UsersIT extends ITBase {
	@Test
	public void getAllUsers() {
		var users = when()
				.get("/users")
				.then().assertThat()
				.statusCode(HTTP_OK)
				.extract().jsonPath().getList("_embedded.users", User.class);

		assertThat(users)
				.extracting(User::getFirstName)
				.containsOnly("Cristiano", "Gareth", "Sergio", "Karim", "Iker");
	}

	@Test
	public void getUserById() {
		var user = given()
				.pathParam("id", "3060754f-8543-416f-b4f5-6f762c620f01")
				.when()
					.get("/users/{id}")
				.then().assertThat()
					.statusCode(HTTP_OK)
					.extract().body().as(User.class);

		assertThat(user.getFirstName()).isEqualTo("Cristiano");
	}
}
