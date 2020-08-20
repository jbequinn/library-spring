package com.dodecaedro.library.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.stream.Stream;

import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.fail;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@TestMethodOrder(MethodOrderer.Random.class)
public abstract class AbstractIntegrationTest {
	@Autowired
	private ObjectMapper objectMapper;
	@LocalServerPort
	private int port;

	private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12.4-alpine")
			.withUsername("libraryuser")
			.withPassword("librarypassword")
			.withDatabaseName("librarydb");

	private static final ElasticsearchContainer elasticsearchContainer =
			new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.9.0")
					.withEnv("cluster.name", "integration-test-cluster");

	@DynamicPropertySource
	static void testcontainersProperties(DynamicPropertyRegistry registry) {
		Stream.of(postgresContainer, elasticsearchContainer)
				.parallel()
				.forEach(GenericContainer::start);

		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
		registry.add("spring.data.elasticsearch.cluster-name", () -> "integration-test-cluster");
		registry.add("spring.data.elasticsearch.cluster-nodes", () -> "127.0.0.1:" + elasticsearchContainer.getMappedPort(9200));
	}

	@PostConstruct
	void configureRestAssured() {
		RestAssured.port = port;
		RestAssured.config = RestAssuredConfig.config()
				.logConfig(logConfig()
						.enableLoggingOfRequestAndResponseIfValidationFails())
				.objectMapperConfig(objectMapperConfig()
						.jackson2ObjectMapperFactory((cls, charset) -> objectMapper));
		RestAssured.basePath = "/api";
	}

	@BeforeAll
	public static void setDefaultTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone(UTC)); // because DBUnit doesn't handle timezones correctly
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
