package com.dodecaedro.library.pojo;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"isbn"})
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book implements Serializable {
  @Id
  @NotNull
  @Column(name = "id")
  @GeneratedValue
  private Long bookId;

  @NotNull @Column private String title;

  @NotNull @Column private String isbn;

  @NotNull
  @Column(name = "bought_date")
  private ZonedDateTime dateTimeBought;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
  private List<Borrow> borrows;

  @PrePersist
  void createdAt() {
    if (this.dateTimeBought == null) {
      this.dateTimeBought = ZonedDateTime.now();
    }
  }
}
