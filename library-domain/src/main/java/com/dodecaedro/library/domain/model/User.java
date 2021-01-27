package com.dodecaedro.library.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"borrows", "fines"})
@Entity
public class User {
	@Builder.Default
	@Id
	private UUID id = UUID.randomUUID();
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	private String address;
	private String phone;
	private String email;
	@Column(name = "join_date")
	private ZonedDateTime joinDateTime;
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Borrow> borrows;
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Fine> fines;
}
