package src.com.dodecaedro.library;

import com.dodecaedro.library.LibrarySpringApplication;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;

@SpringBootTest(classes = LibrarySpringApplication.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
public class UsersIT {
  @LocalServerPort
  private int port;

  @Before
  public void setUp() {
    RestAssured.port = port;
  }

  @Test
  @DataSet(value ="users_and_books.yml", strategy = SeedStrategy.CLEAN_INSERT,
    disableConstraints = true, cleanAfter = true, transactional = true)
  public void getAllUsers() {
    when()
      .get("/users")
      .then()
      .statusCode(HttpStatus.SC_OK);
  }
}
