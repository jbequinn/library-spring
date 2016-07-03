package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.BorrowId;
import com.dodecaedro.library.data.pojo.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = Borrow.class, idClass = BorrowId.class)
public interface BorrowRepository {
  Borrow save(Borrow borrow);
  List<Borrow> findAll();
  List<Borrow> findAll(Specification<Borrow> specification);
  Optional<Borrow> findTopByUserAndBookAndActualReturnDateIsNullOrderByBorrowDateDesc(User user, Book book);
  Long countByUserAndActualReturnDateIsNullOrderByBorrowDateDesc(User user);
  Long count(Specification<Borrow> specification);
}
