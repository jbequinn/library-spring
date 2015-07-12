package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.BorrowId;
import com.dodecaedro.library.data.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = Borrow.class, idClass = BorrowId.class)
public interface BorrowRepository {
  Borrow save(Borrow borrow);
  List<Borrow> findAll();
  Borrow findTopByUserAndBookAndActualReturnDateIsNullOrderByBorrowDateDesc(User user, Book book);
  @Query("select b from Borrow b where b.user = :user and b.expectedReturnDate < CURRENT_TIMESTAMP and b.actualReturnDate is null")
  List<Borrow> findUserExpiredBorrows(@Param("user") User user);
}
