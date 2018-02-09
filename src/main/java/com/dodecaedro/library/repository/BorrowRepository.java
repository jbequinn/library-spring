package com.dodecaedro.library.repository;

import com.dodecaedro.library.pojo.Book;
import com.dodecaedro.library.pojo.Borrow;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "borrows", path = "borrows")
public interface BorrowRepository extends PagingAndSortingRepository<Book, Long> {
  Borrow save(Borrow borrow);

  long count(Specification<Borrow> specification);

  List<Borrow> findAll(Specification<Borrow> specification);
}
