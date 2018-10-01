package com.dodecaedro.library.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.TimeZone;

import static io.restassured.config.RestAssuredConfig.config;
import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {UsersIT.Initializer.class})
@DBRider
public abstract class ITBase {
	static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:11")
			.withUsername("libraryuser")
			.withPassword("librarypassword")
			.withDatabaseName("librarydb");

	// workaround not to create one container per test class
	// see: https://github.com/testcontainers/testcontainers-java/issues/417
	static {
		postgresContainer.start();
	}

	@Autowired
	ObjectMapper objectMapper;
	@LocalServerPort
	private int port;

	@BeforeClass
	public static void setDefaultTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone(UTC)); // because DBUnit doesn't handle timezones correctly
	}

	@Before
	public void setUpRestAssured() {
		RestAssured.port = port;
		RestAssured.config = config()
				.objectMapperConfig(config().getObjectMapperConfig()
						.jackson2ObjectMapperFactory((cls, charset) -> objectMapper));
		RestAssured.basePath = "/api";
	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + postgresContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgresContainer.getUsername(),
					"spring.datasource.password=" + postgresContainer.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			fail();
			return null;
		}
	}
}
