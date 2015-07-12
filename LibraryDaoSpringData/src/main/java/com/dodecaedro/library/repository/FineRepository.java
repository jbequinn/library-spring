package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.Borrow;
import com.dodecaedro.library.data.pojo.Fine;
import com.dodecaedro.library.data.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = Fine.class, idClass = Integer.class)
public interface FineRepository {
  Fine findTopByUserOrderByFineEndDateDesc(User user);
  Fine save(Fine fine);
  @Query("select f from Fine f where f.user = :user and CURRENT_TIMESTAMP BETWEEN f.fineStartDate and f.fineEndDate")
  List<Fine> findActiveFines(@Param("user") User user);
  @Query("select f from Fine f left join f.user u left join u.borrows b where b = :borrow")
  Fine findByBorrow(@Param("borrow") Borrow borrow);
}
