package com.example.P2PNC.repository;

import com.example.P2PNC.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Optional<Book> findByAuthor(String author);
    Optional<Book> findOneByGenre(String genre);
}
