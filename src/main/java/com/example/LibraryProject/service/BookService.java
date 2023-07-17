package com.example.LibraryProject.service;


import com.example.LibraryProject.exceptions.LibraryNotFoundException;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.dto.BookDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public interface BookService {
    /////////////////////////// Get methods ///////////////////////////
    CollectionModel<EntityModel<Book>> getBooks();
    EntityModel<BookDTO> singleBookInfo(UUID id) throws ChangeSetPersister.NotFoundException;
    ResponseEntity<EntityModel<Book>> getBookById(@PathVariable UUID bookId);
    CollectionModel<EntityModel<BookDTO>> getBooksByAvailability(@PathVariable(required = false) String status);
    CollectionModel<EntityModel<BookDTO>> findBooksByPriceBetween(@RequestParam(required = false) Double maxprice, Double minprice);
    /////////////////////////// Delete ///////////////////////////
    void deleteBookById(UUID id) throws LibraryNotFoundException;
    /////////////////////////// Post ///////////////////////////
    Book saveNewBook(Book book);
    void addBookToCustomerFromApi(String customerId, String bookId) throws ChangeSetPersister.NotFoundException;
    /////////////////////////// Put ///////////////////////////
    BookDTO updateBookPrice(UUID bookId, Double newPrice);
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////









    CompletableFuture<ResponseEntity<List<Book>>> getAPIBooks();
}