package com.dodecaedro.library.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "id")
@Entity
@Table(name = "borrow")
@Builder
public class Borrow implements Serializable {
  @EmbeddedId
  private BorrowId id;

  @ManyToOne
  @JoinColumn(name = "book_id", insertable = false, updatable = false)
  @JsonIgnoreProperties("borrows")
  private Book book;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  @JsonIgnoreProperties("borrows")
  private User user;

  @Column(name = "expected_return_date")
  private ZonedDateTime expectedReturnDate;

  @Column(name = "actual_return_date")
  private ZonedDateTime actualReturnDate;
}
