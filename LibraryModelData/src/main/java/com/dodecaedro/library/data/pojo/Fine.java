package com.dodecaedro.library.data.pojo;

import com.dodecaedro.library.data.pojo.converter.LocalDateTimePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"user", "fineEndDate"})
@Entity
@Table(name = "FINE")
public class Fine {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "fineSeq", sequenceName = "S_FINE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fineSeq")
  private Integer fineId;
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  @Column(name = "FINE_END_DATE")
  private LocalDateTime fineEndDate;
  @Column(name = "USER_ID")
  private Integer userId;
  @OneToOne(fetch=FetchType.LAZY)
  @PrimaryKeyJoinColumn(name="USER_ID", referencedColumnName="ID")
  private User user;
}
