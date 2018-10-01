package com.dodecaedro.library.infrastructure.projection;

import com.dodecaedro.library.domain.model.User;
import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "userExcerpt", types = User.class)
public interface UserExcerptProjection {
	UUID getId();

	String getFirstName();

	String getLastName();

	String getEmail();
}
