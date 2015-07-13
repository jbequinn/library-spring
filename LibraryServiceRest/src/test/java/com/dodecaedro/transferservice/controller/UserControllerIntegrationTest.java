package com.dodecaedro.transferservice.controller;

import com.dodecaedro.library.controller.UserController;
import com.dodecaedro.library.data.pojo.User;
import com.dodecaedro.library.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerIntegrationTest {
  private MockMvc mockMvc;

  @InjectMocks
  private UserController controller;

  @Mock
  private UserRepository repository;

  private static final String USER_URL = "/users/{id}";

  @Before
  public void setup() {
    initMocks(this);
    this.mockMvc = standaloneSetup(controller)
            .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
  }

  @Test
  public void thatViewCustomerUsesHttpOK() throws Exception {
    when(repository.findOne(any(Integer.class))).thenReturn(new User());
    this.mockMvc.perform(
      get(USER_URL, "1")
        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }
}
