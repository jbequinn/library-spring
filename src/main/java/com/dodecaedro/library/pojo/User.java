package com.dodecaedro.library.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Data
@EqualsAndHashCode(of = {"lastName", "joinDateTime"})
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
public class User implements Serializable {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "userSeq", sequenceName = "S_USER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
  private Long userId;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "ADDRESS")
  private String address;

  @Column(name = "PHONE")
  private String phone;

  @Email
  @Column(name = "EMAIL")
  private String email;

  @Column(name = "JOIN_DATE")
  private ZonedDateTime joinDateTime;

  @OneToMany(
    mappedBy = "user",
    cascade = {REMOVE, PERSIST}
  )
  @JsonIgnoreProperties("user")
  private List<Borrow> borrows;

  @PrePersist
  void createdAt() {
    if (this.joinDateTime == null) {
      this.joinDateTime = ZonedDateTime.now();
    }
  }
}
