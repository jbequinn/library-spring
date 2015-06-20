package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"isbn"})
@Entity
@Table(name = "BOOK")
public class Book {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "bookSeq", sequenceName = "S_BOOK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookSeq")
  private Integer bookId;
  @Column(name = "TITLE")
  private String title;
  @Column(name = "ISBN")
  private String isbn;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "BOUGHT_DATE")
  private LocalDateTime dateTimeBought;

  @OneToMany(mappedBy = "book")
  private List<Borrow> borrows;
}
