package com.dodecaedro.library.infrastructure.search

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.*

@Document(indexName = "library")
data class BookSearchDocument(
	@Id
	var id: UUID?,
	@Field(type = FieldType.Text)
	val title: String?
)
