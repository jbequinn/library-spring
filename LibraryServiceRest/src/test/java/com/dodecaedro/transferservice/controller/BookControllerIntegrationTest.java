package com.dodecaedro.transferservice.controller;

import com.dodecaedro.library.controller.BookController;
import com.dodecaedro.library.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class BookControllerIntegrationTest {
  private MockMvc mockMvc;

  @InjectMocks
  private BookController controller;

  @Mock
  private BookRepository repository;

  private static final String BOOKS_URL = "/books";

  @Before
  public void setup() {
    initMocks(this);
    this.mockMvc = standaloneSetup(controller)
      .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
  }

  @Test
  public void thatViewBooksUsesHttpOK() throws Exception {
    when(repository.findAll()).thenReturn(new ArrayList<>());
    this.mockMvc.perform(
      get(BOOKS_URL)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
}
