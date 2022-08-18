package com.dodecaedro.library.infrastructure.repository

import com.dodecaedro.library.domain.model.Book
import com.dodecaedro.library.domain.repository.BookRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

@RepositoryRestResource(
	collectionResourceRel = "books",
	path = "books"
)
interface JpaBookRepository: PagingAndSortingRepository<Book, UUID>, BookRepository {
	@Cacheable("books")
	override fun findBookById(bookId: UUID) = findByIdOrNull(bookId)

	@CachePut(value = ["books"], key = "#p0.id")
	override fun save(book: Book): Book

	override fun findAll(): List<Book>

	@Cacheable("books")
	override fun findAllById(ids: Iterable<UUID>): List<Book>

	@CacheEvict("books")
	override fun delete(book: Book)
}
