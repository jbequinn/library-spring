package com.dodecaedro.library.repository;

import com.dodecaedro.library.pojo.Borrow;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long>, QuerydslPredicateExecutor<Borrow> {
  Borrow save(Borrow borrow);

  long count(Predicate predicate);

  List<Borrow> findAll(Specification<Borrow> specification);
}
