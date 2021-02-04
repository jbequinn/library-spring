package com.dodecaedro.library.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user"})
@Entity
public class Fine {
	@Builder.Default
	@Id
	private UUID id = UUID.randomUUID();
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;
	@Column(name = "fine_start_date")
	private ZonedDateTime fineStartDate;
	@Column(name = "fine_end_date")
	private ZonedDateTime fineEndDate;
}
