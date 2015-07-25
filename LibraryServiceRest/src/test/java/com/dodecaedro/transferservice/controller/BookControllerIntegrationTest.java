package com.dodecaedro.transferservice.controller;

import com.dodecaedro.library.controller.BookController;
import com.dodecaedro.library.data.pojo.Book;
import com.dodecaedro.library.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class BookControllerIntegrationTest {
  private MockMvc mockMvc;

  @InjectMocks
  private BookController controller;

  @Mock
  private BookRepository repository;

  private static final String BOOK_URL = "/books/{id}";
  private static final String BOOKS_URL = "/books";

  @Before
  public void setup() {
    initMocks(this);
    this.mockMvc = standaloneSetup(controller)
      .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
  }

  @Test
  public void thatViewBookUsesHttpOK() throws Exception {
    when(repository.findOne(any(Integer.class))).thenReturn(new Book());
    this.mockMvc.perform(
      get(BOOK_URL, "1")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  public void thatViewBooksUsesHttpOK() throws Exception {
    when(repository.findAll()).thenReturn(new ArrayList<>());
    this.mockMvc.perform(
      get(BOOKS_URL)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  public void thatViewBooksUsesHttpNoContent() throws Exception {
    this.mockMvc.perform(
      delete(BOOK_URL, 1)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  public void thatCreateBookUsesHttpCreated() throws Exception {
    this.mockMvc.perform(
      post(BOOKS_URL)
        .content(StreamUtils.copyToString(BookControllerIntegrationTest.class.getResourceAsStream("/book.json"), Charset.defaultCharset()))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isCreated());
  }
}
