package com.dodecaedro.library.infrastructure.controller;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.domain.repository.BookRepository;
import com.dodecaedro.library.infrastructure.repository.ElasticBookSearchDocumentRepository;
import com.dodecaedro.library.infrastructure.repository.JpaBookRepository;
import com.dodecaedro.library.infrastructure.search.BookSearchDocument;
import com.dodecaedro.library.infrastructure.search.BookToSearchDocumentConverter;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
@RequestMapping(path = "books")
public class BookController {
	private final JpaBookRepository bookRepository;
	private final ElasticBookSearchDocumentRepository searchDocumentRepository;
	private final BookToSearchDocumentConverter bookToSearchDocumentConverter;

	public BookController(JpaBookRepository bookRepository,
												ElasticBookSearchDocumentRepository searchDocumentRepository,
												BookToSearchDocumentConverter bookToSearchDocumentConverter) {
		this.bookRepository = bookRepository;
		this.searchDocumentRepository = searchDocumentRepository;
		this.bookToSearchDocumentConverter = bookToSearchDocumentConverter;
	}

	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	@CachePut(value = "books", key = "#p0.Id")
	public PersistentEntityResource saveBook(@RequestBody Book book, PersistentEntityResourceAssembler resourceAssembler) {
		searchDocumentRepository.save(bookToSearchDocumentConverter.toSearchDocument(book));
		return resourceAssembler.toResource(bookRepository.save(book));
	}

	@GetMapping("search")
	@ResponseBody
	public ResponseEntity<?> findBooksByAttributes(@RequestParam("title") String title) {
		var bookIds = searchDocumentRepository.findByTitle(title).stream()
			.map(BookSearchDocument::getId)
			.collect(Collectors.toList());

		var books = bookIds.isEmpty() ? Collections.emptyList() : bookRepository.findAllById(bookIds);
		var resources = new Resources<>(books);
		resources.add(linkTo(methodOn(BookController.class).findBooksByAttributes(title)).withSelfRel());
		return ResponseEntity.ok(resources);
	}
}
