package com.example.LibraryProject.repository;

import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book,UUID> {
    List<Book> findByBookStatus(BookStatus bookStatus); // For my getBooksByAvailability method;
    Book findByBookTitle(String bookTitle);

}
