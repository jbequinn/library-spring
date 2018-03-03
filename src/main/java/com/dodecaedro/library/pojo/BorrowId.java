package com.dodecaedro.library.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Embeddable
public class BorrowId implements Serializable {
  @Column(name = "book_id")
  private Long bookId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "borrow_date")
  private ZonedDateTime borrowDate;
}
