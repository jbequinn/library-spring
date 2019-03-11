package com.dodecaedro.library.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.TimeZone;

import static io.restassured.config.RestAssuredConfig.config;
import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {UsersIT.Initializer.class})
@DBRider
public abstract class ITBase {
	@Autowired
	private ObjectMapper objectMapper;
	@LocalServerPort
	private int port;

	public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:11.1-alpine")
		.withUsername("libraryuser")
		.withPassword("librarypassword")
		.withDatabaseName("librarydb");

	public static ElasticsearchContainer elasticsearchContainer =
		new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.2.2")
		.withEnv("cluster.name", "integration-test-cluster");

	@BeforeAll
	public static void containerStart() {
		// teardown done automatically on jvm exit
		postgresContainer.start();
		elasticsearchContainer.start();
	}

	@BeforeAll
	public static void setDefaultTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone(UTC)); // because DBUnit doesn't handle timezones correctly
	}

	@BeforeEach
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
				"spring.datasource.password=" + postgresContainer.getPassword(),
				"spring.data.elasticsearch.cluster-name=integration-test-cluster",
				"spring.data.elasticsearch.cluster-nodes=localhost:" + elasticsearchContainer.getMappedPort(9300)
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
