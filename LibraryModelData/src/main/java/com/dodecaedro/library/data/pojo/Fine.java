package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.views.ModelViews;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fineId")
@Data
@EqualsAndHashCode(of = {"user", "fineEndDate"})
@Entity
@Table(name = "FINE")
public class Fine implements Serializable {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "fineSeq", sequenceName = "S_FINE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fineSeq")
  private Integer fineId;

  @Column(name = "FINE_START_DATE")
  @JsonView(ModelViews.BasicFineView.class)
  private ZonedDateTime fineStartDate;

  @Column(name = "FINE_END_DATE")
  @JsonView(ModelViews.BasicFineView.class)
  private ZonedDateTime fineEndDate;

  @Column(name = "USER_ID")
  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn(name = "USER_ID", referencedColumnName = "ID")
  private User user;
}
