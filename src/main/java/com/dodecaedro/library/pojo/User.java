package com.dodecaedro.library.pojo;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"lastName", "joinDateTime"})
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements Serializable {
  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "userSeq", sequenceName = "S_USER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
  private Long userId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column private String address;

  @Column private String phone;

  @Email @Column private String email;

  @Column(name = "join_date")
  private ZonedDateTime joinDateTime;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<Borrow> borrows;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<Fine> fines;

  @PrePersist
  void createdAt() {
    if (this.joinDateTime == null) {
      this.joinDateTime = ZonedDateTime.now();
    }
  }
}
