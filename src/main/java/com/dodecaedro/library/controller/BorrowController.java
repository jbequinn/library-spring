package com.dodecaedro.library.controller;

import com.dodecaedro.library.service.LibraryService;
import org.springframework.stereotype.Controller;

@Controller
public class BorrowController {
  private final LibraryService libraryService;

  public BorrowController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }
}
