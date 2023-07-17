package com.example.LibraryProject.service;

import com.example.LibraryProject.pojo.Book;
import com.fasterxml.jackson.core.JsonFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

@Service
public class GoogleBooksServiceImpl implements GoogleBooksService {



    @Override
    public CollectionModel<EntityModel<Book>> getGoogleBooks() {
        return null;
    }
}
