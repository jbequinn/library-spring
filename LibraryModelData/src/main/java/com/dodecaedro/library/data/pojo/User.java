package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"lastName", "joinDateTime"})
@Entity
@Table(name = "USER")
public class User {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "userSeq", sequenceName = "S_USER")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
  private Integer userId;
  @Column(name = "FIRST_NAME")
  private String firstName;
  @Column(name = "LAST_NAME")
  private String lastName;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "PHONE")
  private String phone;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "JOIN_DATE")
  private LocalDateTime joinDateTime;

  @OneToMany(mappedBy = "user")
  private List<Borrow> borrows;
}
