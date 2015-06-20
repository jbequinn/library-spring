package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"userId", "bookId", "borrowDate"})
public class BorrowId implements Serializable {
  @Column(name="USER_ID")
  private Integer userId;
  @Column(name="BOOK_ID")
  private Integer bookId;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name="BORROW_DATE")
  private LocalDateTime borrowDate;
}
