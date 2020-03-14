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
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.stream.Stream;

import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@TestMethodOrder(MethodOrderer.Random.class)
public abstract class ITBase {
	@Autowired
	private ObjectMapper objectMapper;
	@LocalServerPort
	private int port;

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

	public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:12.2-alpine")
			.withUsername("libraryuser")
			.withPassword("librarypassword")
			.withDatabaseName("librarydb");

	public static ElasticsearchContainer elasticsearchContainer =
			new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.6.1")
					.withEnv("cluster.name", "integration-test-cluster");

	@BeforeAll
	public static void containerStart() {
		// teardown done automatically on jvm exit
		Stream.of(postgresContainer, elasticsearchContainer)
				.parallel()
				.forEach(GenericContainer::start);

		System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
		System.setProperty("spring.datasource.username", postgresContainer.getUsername());
		System.setProperty("spring.datasource.password", postgresContainer.getPassword());
		System.setProperty("spring.data.elasticsearch.cluster-name", "integration-test-cluster");
		System.setProperty("spring.data.elasticsearch.cluster-nodes", "localhost:" + elasticsearchContainer.getMappedPort(9300));
		System.setProperty("spring.data.elasticsearch.client-url", "localhost:" + elasticsearchContainer.getMappedPort(9200));
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
