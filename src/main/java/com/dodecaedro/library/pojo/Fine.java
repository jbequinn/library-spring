package com.dodecaedro.library.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(of = {"user", "fineEndDate"})
@Entity
@Table(name = "FINE")
public class Fine implements Serializable {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "fineSeq", sequenceName = "S_FINE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fineSeq")
  private Long fineId;

  @Column(name = "FINE_START_DATE")
  private ZonedDateTime fineStartDate;

  @Column(name = "FINE_END_DATE")
  private ZonedDateTime fineEndDate;

  @OneToOne
  private User user;
}
