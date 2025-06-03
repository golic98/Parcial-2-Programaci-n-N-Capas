package com.example.P2PNC.controller;


import com.example.P2PNC.config.JwtUtils;
import com.example.P2PNC.model.Book;
import com.example.P2PNC.model.dto.Message;
import com.example.P2PNC.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/registerBook")
    public ResponseEntity<?> register(@RequestBody Book book) {
        try {
            Book saveBook = bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveBook);
        } catch (Message e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al registrar el libro.");
        }
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<?> updateBook(@PathVariable String isbn, @RequestBody Book bodyBook) {
        try {
            Book book = bookService.updateBook(isbn, bodyBook);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado al registrar el libro.");
        }
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
        try {
            bookService.deleteOneBook(isbn);
            return ResponseEntity.ok("Libro eliminado");
        } catch(Message e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al iniciar sesión.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> users = bookService.getAllBooks();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getOneAuthor(@RequestParam ("author") String author) {
        List<Book> book = Collections.singletonList(bookService.findByAuthor(author));
        return ResponseEntity.ok(book);
    }

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getOneGenrer(@RequestParam ("genre") String genre) {
        List<Book> book = Collections.singletonList(bookService.findByGenre(genre));
        return ResponseEntity.ok(book);
    }
}