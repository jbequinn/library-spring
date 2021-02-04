package com.dodecaedro.library.infrastructure.projection;

import com.dodecaedro.library.domain.model.User;
import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "userSummary", types = User.class)
public interface UserSummary {
	UUID getId();

	String getFirstName();

	String getLastName();

	String getEmail();
}
