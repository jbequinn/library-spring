package com.dodecaedro.library.infrastructure;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import static java.util.Objects.requireNonNull;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {
	private final Environment environment;

	public RestClientConfig(Environment environment) {
		this.environment = environment;
	}

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {
		final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(requireNonNull(environment.getProperty("spring.data.elasticsearch.client-url")))
				.build();

		return RestClients.create(clientConfiguration).rest();
	}
}
