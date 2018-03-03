package com.dodecaedro.library.pojo.projection;

import com.dodecaedro.library.pojo.User;
import org.springframework.data.rest.core.config.Projection;

import java.time.ZonedDateTime;

@Projection(name = "userExcerpt", types = User.class)
public interface UserExcerptProjection {
  Long getUserId();

  String getFirstName();

  String getLastName();

  String getEmail();
}
