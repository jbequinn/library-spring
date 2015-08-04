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
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Data
@EqualsAndHashCode(of = {"lastName", "joinDateTime"})
@ToString(exclude="borrows")
@Entity
@Table(name = "USER")
public class User implements Serializable {
  @Id
  @Column(name = "ID")
  @JsonView(ModelViews.BasicUserView.class)
  @SequenceGenerator(name = "userSeq", sequenceName = "S_USER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
  private Integer userId;

  @Column(name = "FIRST_NAME")
  @JsonView(ModelViews.BasicUserView.class)
  private String firstName;

  @Column(name = "LAST_NAME")
  @JsonView(ModelViews.BasicUserView.class)
  private String lastName;

  @Column(name = "ADDRESS")
  @JsonView(ModelViews.BasicUserView.class)
  private String address;

  @Column(name = "PHONE")
  @JsonView(ModelViews.BasicUserView.class)
  private String phone;

  @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
    +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
    +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
    message="invalid email")
  @Column(name = "EMAIL")
  @JsonView(ModelViews.BasicUserView.class)
  private String email;

  @JsonFormat(pattern = DateFormat.DATE_TIME)
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "JOIN_DATE")
  @JsonView(ModelViews.BasicUserView.class)
  private LocalDateTime joinDateTime;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Borrow> borrows;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Fine> fines;
}
