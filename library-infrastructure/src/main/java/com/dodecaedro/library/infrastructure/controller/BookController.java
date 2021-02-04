package com.dodecaedro.library.infrastructure.controller;

import com.dodecaedro.library.domain.model.Book;
import com.dodecaedro.library.infrastructure.repository.ElasticBookSearchDocumentRepository;
import com.dodecaedro.library.infrastructure.repository.JpaBookRepository;
import com.dodecaedro.library.infrastructure.search.BookSearchDocument;
import com.dodecaedro.library.infrastructure.search.BookToSearchDocumentConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController // override some methods from the @RepositoryRestResource
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {
	@NonNull private final JpaBookRepository bookRepository;
	@NonNull private final ElasticBookSearchDocumentRepository searchDocumentRepository;
	@NonNull private final BookToSearchDocumentConverter bookToSearchDocumentConverter;

	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	@CachePut(value = "books", key = "#p0.Id")
	public PersistentEntityResource saveBook(@RequestBody Book book, PersistentEntityResourceAssembler resourceAssembler) {
		searchDocumentRepository.save(bookToSearchDocumentConverter.toSearchDocument(book));
		return resourceAssembler.toModel(bookRepository.save(book));
	}

	@GetMapping("search")
	public ResponseEntity<?> findBooksByAttributes(@RequestParam("title") String title) {
		var bookIds = searchDocumentRepository.findByTitle(title).stream()
			.map(BookSearchDocument::getId)
			.collect(Collectors.toList());

		var books = bookIds.isEmpty() ? List.of() : bookRepository.findAllById(bookIds);

		return ResponseEntity
				.ok(CollectionModel.of(books)
						.add(linkTo(methodOn(BookController.class)
								.findBooksByAttributes(title))
								.withSelfRel()));
	}
}
