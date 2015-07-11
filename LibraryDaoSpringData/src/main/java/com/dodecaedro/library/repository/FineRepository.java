package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.Fine;
import com.dodecaedro.library.data.pojo.User;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Fine.class, idClass = Integer.class)
public interface FineRepository {
  Fine findTopByUserOrderByFineEndDateDesc(User user);
  Fine save(Fine fine);
}
