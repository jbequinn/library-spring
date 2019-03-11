package com.dodecaedro.library.infrastructure.repository;

import com.dodecaedro.library.infrastructure.search.BookSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface ElasticBookSearchDocumentRepository extends ElasticsearchRepository<BookSearchDocument, UUID> {
	List<BookSearchDocument> findByTitle(String title);
}
