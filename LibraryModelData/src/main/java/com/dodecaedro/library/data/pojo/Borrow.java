package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BORROW")
@IdClass(BorrowId.class)
public class Borrow implements Serializable {
  @Id
  @Column(name="USER_ID")
  private Integer userId;
  @Id
  @Column(name="BOOK_ID")
  private Integer bookId;
  @Id
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name="BORROW_DATE")
  private LocalDateTime borrowDate;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name="EXPECTED_RETURN_DATE")
  private LocalDateTime expectedReturnDate;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name="ACTUAL_RETURN_DATE")
  private LocalDateTime actualReturnDate;

  @ManyToOne
  @PrimaryKeyJoinColumn(name="USER_ID", referencedColumnName="ID")
  /* if this JPA model doesn't create a table for the "PROJ_EMP" entity,
  *  please comment out the @PrimaryKeyJoinColumn, and use the ff:
  *  @JoinColumn(name = "employeeId", updatable = false, insertable = false)
  * or @JoinColumn(name = "employeeId", updatable = false, insertable = false, referencedColumnName = "id")
  */
  private User user;

  @ManyToOne
  @PrimaryKeyJoinColumn(name="BOOK_ID", referencedColumnName="ID")
  /* the same goes here:
  *  if this JPA model doesn't create a table for the "PROJ_EMP" entity,
  *  please comment out the @PrimaryKeyJoinColumn, and use the ff:
  *  @JoinColumn(name = "projectId", updatable = false, insertable = false)
  * or @JoinColumn(name = "projectId", updatable = false, insertable = false, referencedColumnName = "id")
  */
  private Book book;

}