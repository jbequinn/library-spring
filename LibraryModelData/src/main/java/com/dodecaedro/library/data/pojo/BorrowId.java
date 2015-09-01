package com.dodecaedro.library.data.pojo;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.ZonedDateTime;

@EqualsAndHashCode(of = {"userId", "bookId", "borrowDate"})
public class BorrowId implements Serializable {
  private Integer userId;
  private Integer bookId;
  private ZonedDateTime borrowDate;
}
