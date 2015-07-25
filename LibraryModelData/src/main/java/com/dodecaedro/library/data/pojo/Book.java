package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bookId")
@XmlRootElement
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

  @XmlTransient
  @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
  private List<Borrow> borrows;
}
