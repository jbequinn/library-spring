package com.dodecaedro.library.pojo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"bookId", "userId", "borrowDate"})
@EqualsAndHashCode(of = {"bookId", "userId", "borrowDate"})
@Embeddable
public class BorrowId implements Serializable {
  @Column(name = "book_id")
  private Long bookId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "borrow_date")
  private ZonedDateTime borrowDate;

  public BorrowId(Long bookId, Long userId) {
    this.bookId = bookId;
    this.userId = userId;
    this.borrowDate = ZonedDateTime.now();
  }
}
