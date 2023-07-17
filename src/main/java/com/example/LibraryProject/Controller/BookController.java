package com.example.LibraryProject.Controller;
import com.example.LibraryProject.Assembler.BookEntityAssembler;
import com.example.LibraryProject.Assembler.dto.BookDTOAssembler;
import com.example.LibraryProject.exceptions.LibraryNotFoundException;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.BookStatus;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.GoogleBookItem;
import com.example.LibraryProject.repository.BookRepository;
import com.example.LibraryProject.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.LibraryProject.API.GoogleBookService;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/books")
public class BookController {
    private final RestTemplate restTemplate;
    @Autowired
    private BookService bookService;
    @Autowired
    private final GoogleBookService googleBookService;
    private final BookRepository bookRepository; // Access to the Database;
    private final BookEntityAssembler bookEntityAssembler; // For converting pojos into Entity/Collection Models;
    private final BookDTOAssembler bookDTOAssembler; // For converting pojos into DTOs and returning answers to the users;

        public BookController(GoogleBookService googleBookService, BookRepository bookRepository,
                              BookEntityAssembler bookEntityAssembler, BookDTOAssembler bookDTOAssembler,
                              RestTemplateBuilder restTemplateBuilder) {
            this.googleBookService = googleBookService;
            this.bookRepository = bookRepository;
            this.bookEntityAssembler = bookEntityAssembler;
            this.bookDTOAssembler = bookDTOAssembler;
            this.restTemplate = restTemplateBuilder.build();
}



    ///////////////////   ///////////     GET BOOKS    /////////////////////              http://localhost:8080/books
    /**
     * In this method we get all books in our library.
     * @return stat us 200, all books from the database.
     */
    @GetMapping
        public ResponseEntity<CollectionModel<EntityModel<Book>>> getBooks() {
            return ResponseEntity.ok().body(bookService.getBooks());
    }

    ///////////////   //////////     SINGLE BOOK INFO  (By id)  ///////////////////         http://localhost:8080/books/1/info
    /**
     * In this method we get DTO info about a single book in the library, by its ID.
     * @return status 200, and the book DTO.
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<EntityModel<BookDTO>> singleBookInfo(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok().body(bookService.singleBookInfo(id));
    }

    ///       ///////////     GET SINGLE BOOK  (By id)   ///////////////////   http://localhost:8080/books/132
    /**
     * In this method we get a single book by its ID.
     * @param bookId -> Expects to get the ID of the book.
     * @return status 200, and the book by its ID.
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<EntityModel<Book>> getBookById(@PathVariable UUID bookId) {
        return (bookService.getBookById(bookId));
    }  //////// http://localhost:8080/books/132


    //////////////////   GET EXTERNAL LIBRARY    /////////////////////     http://localhost:8080/books/externallibrary    // optional: ?maxprice=40&minprice=8.5
    /**
     * In this method we get all books from the external library.
     * @return stat us 200, all books from the API.
     */
    @GetMapping("/externallibrary")
    public CompletableFuture<ResponseEntity<CollectionModel<EntityModel<GoogleBookItem>>>> getExternalLibraryBooks(@RequestParam(required = false) Double maxprice, Double minprice) {
        return googleBookService.getExternalLibraryBooks(maxprice, minprice)
                                .thenApply(items -> {
                                       List<EntityModel<GoogleBookItem>> bookItems = items.stream()
                                                                                          .map(bookItem -> EntityModel.of(bookItem,
                                                                                                  linkTo(methodOn(BookController.class).getExternalLibraryBooks(maxprice, minprice)).withSelfRel()))
                                                                                          .collect(Collectors.toList());
                                       CollectionModel<EntityModel<GoogleBookItem>> collectionModel = CollectionModel.of(bookItems,
                                               linkTo(methodOn(BookController.class).getExternalLibraryBooks(maxprice, minprice)).withSelfRel());
                                       return ResponseEntity.ok(collectionModel);
                                   });
    }

    //////////////////   GET EXTERNAL BOOKS BY ID    /////////////////////
    /**
     * In this method we get single book from the external library by its ID.
     * @param bookId -> Expects to get the ID of the book.
     * @return status 200, and the book by its ID.
     */
    @GetMapping("/externallibrary/{bookId}")
    public CompletableFuture<ResponseEntity<EntityModel<GoogleBookItem>>> getExternalBookById(@PathVariable String bookId) {
        return googleBookService.getExternalBookById(bookId)
                                .thenApply(bookItem -> EntityModel.of(bookItem,
                                           linkTo(methodOn(BookController.class).getExternalBookById(bookId)).withSelfRel()))
                                .thenApply(ResponseEntity::ok);
    }

    //////////////        ALL BOOKS INFO  //////////////////////                http://localhost:8080/books/info
    /**
     * In this method we get DTOs info about all books in the library.
     * @return status 200, and the books from the library.
     */
    @GetMapping("/info")
    public ResponseEntity<CollectionModel<EntityModel<BookDTO>>> allBooksInfo() {

        return ResponseEntity.ok(bookDTOAssembler.toCollectionModel(
                StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                             .map(BookDTO::new).collect(Collectors.toList())));
    }


    //////////////        GET BOOKS BY AVAILABILITY    ////////////////////////////    http://localhost:8080/books/availability/available
    /**
     * In this method we get all books from our library by their availability status.
     * @param status -> Expects to get the status of the book.
     * @return status 200, and the book by its status.
     */
    @GetMapping("/availability/{status}")
    public ResponseEntity<CollectionModel<EntityModel<BookDTO>>> getBooksByAvailability(@PathVariable(required = false) String status) {
        try {
            BookStatus bookStatus = BookStatus.valueOf(status.toUpperCase());
            List<Book> books = bookRepository.findByBookStatus(bookStatus);

            List<BookDTO> bookDTOs = books.stream()
                                          .map(BookDTO::new)
                                          .collect(Collectors.toList());

            CollectionModel<EntityModel<BookDTO>> bookDTOCollectionModel = bookDTOAssembler.toCollectionModel(bookDTOs);

            return ResponseEntity.ok(bookDTOCollectionModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /////////// ///   GET BOOKS BY PRICE RANGE  ////////////////     http://localhost:8080/books/pricerange?maxprice=9&minprice=4.9
    /**
     * In this method we get all books from our library by their price.
     * @param maxprice -> Expects to get the highest price allowed for a book.
     * @param minprice -> Expects to get the lowest price allowed for a book.
     * @return status 200, and the books by the price range.
     */
    @GetMapping("/pricerange")
    public ResponseEntity<CollectionModel<EntityModel<BookDTO>>> findBooksByPriceBetween(@RequestParam(required = false) Double maxprice, Double minprice) {
        CollectionModel<EntityModel<BookDTO>> bookDTOCollectionModel = bookService.findBooksByPriceBetween(maxprice, minprice);

        return ResponseEntity.ok(bookDTOCollectionModel);
    }

    //////////////        DELETE BOOK BY ID    ////////////////////////////
    /**
     * In this method we delete a book from the database.
     * @param bookId -> Expects to get the id of the book that we want to delete.
     * @return status 202, delete the book from database.
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBookById(@PathVariable UUID bookId) {
        try {
            bookService.deleteBookById(bookId);
            String successMessage = "Book deleted!";
            return ResponseEntity.ok(successMessage);
        } catch (LibraryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //////////////        CREATE BOOK      ////////////////////////////
    /**
     *  In this method we create new book in the database
     * @param book -> Expects Book details.
     * @return status 201, create a new book in the database.
     */
    @PostMapping
    public ResponseEntity<EntityModel<Book>> createBook(@RequestBody Book book) {
        Book savedBook = bookService.saveNewBook(book);
        EntityModel<Book> bookModel = bookEntityAssembler.toModel(savedBook);
        return ResponseEntity.created(bookModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(bookModel);
    }

    //////////////        UPDATE BOOK PRICE    ////////////////////////////   http://localhost:8080/books/e40de376-9b3b-45ca-a79d-eb63e4df59a5/price?newPrice=10
    /**
     * In this method we update the book's price by its ID.
     * @param bookId -> Expects to get ID of the book.
     * @param newPrice -> Expects the new price.
     * @return status 201, updated new book in the database.
     */
    @PutMapping("/{bookId}/price")
    public ResponseEntity<String> updateBookPrice(@PathVariable UUID bookId, @RequestParam Double newPrice) {
        BookDTO updatedBook = bookService.updateBookPrice(bookId, newPrice);
        String successMessage = "Price updated!";
        return ResponseEntity.status(HttpStatus.OK).body(successMessage);
    }


    //////////////        ADD BOOK TO CUSTOMER FROM API    ////////////////////////////
    /**
     * In this method we are adding a book to a customer's book list.
     * @param customerId -> Expects to get ID of the customer.
     * @param bookId -> Expects the ID of the book.
     * @return status 201, update the customer's book list in the database.
     */
    @PostMapping("/{customerId}/add/{bookId}")
    public ResponseEntity<String> addBookToCustomerFromApi(@PathVariable String customerId, @PathVariable String bookId) {
        try {
            bookService.addBookToCustomerFromApi(customerId, bookId);
            String message = "Book added successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

