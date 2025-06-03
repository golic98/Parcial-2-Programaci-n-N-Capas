package com.example.P2PNC.service;


import com.example.P2PNC.config.JwtUtils;
import com.example.P2PNC.model.Book;
import com.example.P2PNC.model.dto.Message;
import com.example.P2PNC.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public Book addBook(@Validated Book book) {
        if(bookRepository.existsByIsbn(book.getIsbn())) {
            throw new Message("El ISBN ya existe");
        }

        if(book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new Message("El nombre es requerido");
        }

        if(book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new Message("El autor es requerido");
        }

        if(book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new Message("El isbn es requerido");
        }

        if(book.getPublicationYear() == null) {
            throw new Message("El año de publicación es requerida");
        }

        if(book.getLanguage() == null || book.getLanguage().isEmpty()) {
            throw new Message("El lenguaje es requerido");
        }

        if(book.getPages() == null) {
            throw new Message("El número de páginas es requerido");
        }

        if(book.getGenre() == null) {
            throw new Message("El género del libro es requerido");
        }

        return bookRepository.save(book);
    }

    public Book findByIsbn(String id) {
        return bookRepository.findByIsbn(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    public Book updateBook(String isbn, Book bodyBook) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> {
                    book.setTitle(bodyBook.getTitle());
                    book.setAuthor(bodyBook.getAuthor());
                    book.setIsbn(bodyBook.getIsbn());
                    book.setPublicationYear(bodyBook.getPublicationYear());
                    book.setLanguage(bodyBook.getLanguage());
                    book.setPages(bodyBook.getPages());
                    book.setGenre(bodyBook.getGenre());
                    return bookRepository.save(book);
                }).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    public void deleteOneBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        bookRepository.delete(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book findByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    public Book findByGenre(String genre) {
        return bookRepository.findOneByGenre(genre)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }
}
