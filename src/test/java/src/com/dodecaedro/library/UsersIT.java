package src.com.dodecaedro.library;

import com.dodecaedro.library.LibrarySpringApplication;
import com.dodecaedro.library.pojo.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.TimeZone;

import static io.restassured.RestAssured.when;
import static io.restassured.config.RestAssuredConfig.config;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibrarySpringApplication.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
public class UsersIT {
  @LocalServerPort
  private int port;
  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeClass
  public static void init() {
    TimeZone.setDefault(TimeZone.getTimeZone(UTC)); // because DBUnit doesn't handle timezones correctly
  }

  @Before
  public void setUp() {
    RestAssured.port = port;
    RestAssured.config = config()
      .objectMapperConfig(config().getObjectMapperConfig()
        .jackson2ObjectMapperFactory((cls, charset) -> objectMapper));
    RestAssured.basePath = "/api";
  }

  @Test
  @DataSet(value = "alltables.xml", strategy = SeedStrategy.CLEAN_INSERT,
    disableConstraints = true, cleanAfter = true, transactional = true)
  public void getAllUsers() {
    List<User> users = when()
      .get("/users")
      .then().assertThat()
      .statusCode(HTTP_OK)
      .extract().jsonPath().getList("_embedded.users", User.class);

    assertThat(users).hasSize(5);
  }

  @Test
  @DataSet(value = "alltables.xml", strategy = SeedStrategy.CLEAN_INSERT,
    disableConstraints = true, cleanAfter = true, transactional = true)
  public void getUser() {
    User user = when()
      .get("/users/1")
      .then().assertThat()
      .statusCode(HTTP_OK)
      .extract().body().as(User.class);

    assertThat(user.getFirstName()).isEqualTo("Cristiano");
  }

  /*
  @Test
  @ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/allTables.xml")
  public void shouldExportAllTablesInXMLFormat() {
    //data inserted inside method can be exported
  }
  */
}
