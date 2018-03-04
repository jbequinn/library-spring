package com.dodecaedro.library.repository;

import com.dodecaedro.library.pojo.Borrow;
import com.querydsl.core.types.Predicate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository
    extends JpaRepository<Borrow, Long>, QuerydslPredicateExecutor<Borrow> {

  @CacheEvict({"books", "users"})
  Borrow save(Borrow borrow);

  long count(Predicate predicate);
}
