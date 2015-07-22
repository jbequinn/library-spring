package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@XmlRootElement
@Data
@EqualsAndHashCode(of = {"lastName", "joinDateTime"})
@ToString(exclude="borrows")
@Entity
@Table(name = "USER")
public class User implements Serializable {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "userSeq", sequenceName = "S_USER", allocationSize = 1)
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
  @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
    +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
    +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
    message="invalid email")
  @Column(name = "EMAIL")
  private String email;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "JOIN_DATE")
  private LocalDateTime joinDateTime;

  @XmlTransient
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Borrow> borrows;

  @XmlTransient
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Fine> fines;
}
