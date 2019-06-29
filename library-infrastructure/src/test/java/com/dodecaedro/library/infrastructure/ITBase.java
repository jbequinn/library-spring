package com.dodecaedro.library.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.TimeZone;
import java.util.stream.Stream;

import static io.restassured.config.RestAssuredConfig.config;
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

	public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:11.1-alpine")
		.withUsername("libraryuser")
		.withPassword("librarypassword")
		.withDatabaseName("librarydb");

	public static ElasticsearchContainer elasticsearchContainer =
		new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.7.2")
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

	String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			fail();
			return null;
		}
	}
}
