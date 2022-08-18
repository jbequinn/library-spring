package com.dodecaedro.library.infrastructure.repository

import com.dodecaedro.library.infrastructure.search.BookSearchDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.*

interface ElasticBookSearchDocumentRepository: ElasticsearchRepository<BookSearchDocument, UUID> {
	fun findByTitle(title: String): List<BookSearchDocument>
}
