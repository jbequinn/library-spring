package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fineId")
@XmlRootElement
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
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "FINE_START_DATE")
  private LocalDateTime fineStartDate;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "FINE_END_DATE")
  private LocalDateTime fineEndDate;
  @Column(name = "USER_ID")
  private Integer userId;
  @XmlTransient
  @OneToOne(fetch=FetchType.LAZY)
  @PrimaryKeyJoinColumn(name="USER_ID", referencedColumnName="ID")
  private User user;
}
