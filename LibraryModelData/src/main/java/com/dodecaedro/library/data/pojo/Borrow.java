package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import com.dodecaedro.library.views.ModelViews;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
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
  @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name="BORROW_DATE")
  @JsonView(ModelViews.BasicBorrowView.class)
  private LocalDateTime borrowDate;

  @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name="EXPECTED_RETURN_DATE")
  @JsonView(ModelViews.BasicBorrowView.class)
  private LocalDateTime expectedReturnDate;

  @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name="ACTUAL_RETURN_DATE")
  @JsonView(ModelViews.BasicBorrowView.class)
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
  @JsonView(ModelViews.BasicBorrowView.class)
  @PrimaryKeyJoinColumn(name="BOOK_ID", referencedColumnName="ID")
  /* the same goes here:
  *  if this JPA model doesn't create a table for the "PROJ_EMP" entity,
  *  please comment out the @PrimaryKeyJoinColumn, and use the ff:
  *  @JoinColumn(name = "projectId", updatable = false, insertable = false)
  * or @JoinColumn(name = "projectId", updatable = false, insertable = false, referencedColumnName = "id")
  */
  private Book book;
}
