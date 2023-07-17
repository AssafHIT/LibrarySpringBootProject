package com.example.LibraryProject.service;

import com.example.LibraryProject.pojo.Book;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface GoogleBooksService {
    CollectionModel<EntityModel<Book>> getGoogleBooks();

}
