package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"isbn"})
@ToString(exclude="borrows")
@Entity
@Table(name = "BOOK")
public class Book implements Serializable {
  @Id
  @NotNull
  @Column(name = "ID")
  @SequenceGenerator(name = "bookSeq", sequenceName = "S_BOOK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookSeq")
  private Integer bookId;
  @NotNull
  @Column(name = "TITLE")
  private String title;
  @NotNull
  @Column(name = "ISBN")
  private String isbn;
  @NotNull
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "BOUGHT_DATE")
  private LocalDateTime dateTimeBought;

  @OneToMany(mappedBy = "book")
  private List<Borrow> borrows;
}
