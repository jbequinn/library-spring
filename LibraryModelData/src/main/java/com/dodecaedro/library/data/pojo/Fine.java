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

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

  @JsonFormat(pattern = DateFormat.DATE_TIME)
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "FINE_START_DATE")
  @JsonView(ModelViews.BasicFineView.class)
  private LocalDateTime fineStartDate;

  @JsonFormat(pattern = DateFormat.DATE_TIME)
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "FINE_END_DATE")
  @JsonView(ModelViews.BasicFineView.class)
  private LocalDateTime fineEndDate;

  @Column(name = "USER_ID")
  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn(name = "USER_ID", referencedColumnName = "ID")
  private User user;
}
