package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import com.dodecaedro.library.data.pojo.format.DateFormat;
import com.dodecaedro.library.views.ModelViews;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bookId")
@Data
@EqualsAndHashCode(of = {"isbn"})
@ToString(exclude="borrows")
@Entity
@Table(name = "BOOK")
public class Book implements Serializable {
  @Id
  @NotNull
  @Column(name = "ID")
  @JsonView({ModelViews.BasicBookView.class, ModelViews.BasicBorrowView.class})
  @SequenceGenerator(name = "bookSeq", sequenceName = "S_BOOK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookSeq")
  private Integer bookId;

  @NotNull
  @Column(name = "TITLE")
  @JsonView({ModelViews.BasicBookView.class, ModelViews.BasicBorrowView.class})
  private String title;

  @NotNull
  @Column(name = "ISBN")
  @JsonView({ModelViews.BasicBookView.class, ModelViews.BasicBorrowView.class})
  private String isbn;

  @NotNull
  @JsonFormat(pattern = DateFormat.DATE_TIME)
  @Column(name = "BOUGHT_DATE")
  @JsonView(ModelViews.BasicBookView.class)
  private LocalDateTime dateTimeBought;

  @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
  private List<Borrow> borrows;
}
