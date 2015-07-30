package com.dodecaedro.library.search;

import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.Borrow_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class BorrowSpecifications {
  public static Specification<Borrow> expiredBorrows(final Integer userId) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      LocalDateTime nowDate = LocalDateTime.now();
      return criteriaBuilder.and(criteriaBuilder.isNull(root.get(Borrow_.actualReturnDate)),
        criteriaBuilder.lessThanOrEqualTo(root.get(Borrow_.expectedReturnDate), nowDate),
        criteriaBuilder.equal(root.get(Borrow_.userId), userId));
    };
  }

  public static Specification<Borrow> activeBorrows(final Integer userId) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      LocalDateTime nowDate = LocalDateTime.now();
      return criteriaBuilder.and(criteriaBuilder.isNull(root.get(Borrow_.actualReturnDate)),
        criteriaBuilder.greaterThan(root.get(Borrow_.expectedReturnDate), nowDate),
        criteriaBuilder.equal(root.get(Borrow_.userId), userId));
    };
  }
}
