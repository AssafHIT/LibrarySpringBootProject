package com.example.LibraryProject.service;

import com.example.LibraryProject.API.GoogleBookService;
import com.example.LibraryProject.Assembler.BookEntityAssembler;
import com.example.LibraryProject.Assembler.dto.BookDTOAssembler;
import com.example.LibraryProject.Controller.BookController;
import com.example.LibraryProject.exceptions.LibraryNotFoundException;
import com.example.LibraryProject.exceptions.Messages;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.BookStatus;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.Librarian;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.GoogleBookItem;
import com.example.LibraryProject.repository.BookRepository;
import com.example.LibraryProject.repository.CustomerRepository;
import com.example.LibraryProject.repository.LibrarianRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDTOAssembler bookDTOAssembler;
    private final BookEntityAssembler bookEntityAssembler;
    private final BookRepository bookRepository;
    private final GoogleBookService googleBookService;
    private final RestTemplate restTemplate;
    private final CustomerRepository customerRepository;
    private final LibrarianRepository librarianRepository;

    /**
     *  In this method we return all the books in the library.
     * @return Books
     */
    @Override
    public CollectionModel<EntityModel<Book>> getBooks() {
        List<EntityModel<Book>> books = bookRepository.findAll().stream().map(bookEntityAssembler::toModel).collect(Collectors.toList());
             return CollectionModel.of(books, linkTo(methodOn(BookController.class)
                    .getBooks()).withSelfRel());
    }

    /**
     * In this method we return a book DTO from the library by its ID.
     * @param id -> ID of the book.
     * @return single book DTO.
     */
    @Override
    public EntityModel<BookDTO> singleBookInfo(UUID id) throws LibraryNotFoundException  {
        return bookRepository.findById(id)
                             .map(BookDTO::new)
                             .map(bookDTOAssembler::toModel)
                             .orElseThrow(() -> new LibraryNotFoundException(
                                     (String.format(Messages.NOT_FOUND_MESSAGE, "book", id))
                             ));
    }

    /**
     * In this method we return a single book from the library by its ID.
     * @param bookId -> ID of the book.
     * @return single book.
     */
    @Override
    public ResponseEntity<EntityModel<Book>> getBookById(@PathVariable UUID bookId){
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            EntityModel<Book> bookEntity = bookEntityAssembler.toModel(book);
            return ResponseEntity.ok(bookEntity);
        } else {
            throw new LibraryNotFoundException((String.format(Messages.NOT_FOUND_MESSAGE, "book", bookId)));
        }
    }

    /**
     * In this method we return Books (DTOs) by their availability status.
     * @param status -> status of book in the library. EXAMPLE : AVAILABLE / RESERVED;
     * @return All available/reserved books DTOs.
     */
    @Override
    public CollectionModel<EntityModel<BookDTO>> getBooksByAvailability(@PathVariable(required = false) String status) {
        if (status == null) {
            // No status provided, retrieve all books
            List<Book> books = bookRepository.findAll();

            List<BookDTO> bookDTOs = books.stream()
                                          .map(BookDTO::new)
                                          .collect(Collectors.toList());

            CollectionModel<EntityModel<BookDTO>> bookDTOCollectionModel = bookDTOAssembler.toCollectionModel(bookDTOs);

            return CollectionModel.of(bookDTOCollectionModel, linkTo(methodOn(BookController.class)
                    .getBooksByAvailability(null)).withSelfRel());
        } else {
            // Status provided, retrieve books by status
            BookStatus bookStatus = BookStatus.valueOf(status);
            List<Book> books = bookRepository.findByBookStatus(bookStatus);

            List<BookDTO> bookDTOs = books.stream()
                                          .map(BookDTO::new)
                                          .collect(Collectors.toList());

            CollectionModel<EntityModel<BookDTO>> bookDTOCollectionModel = bookDTOAssembler.toCollectionModel(bookDTOs);

            return CollectionModel.of(bookDTOCollectionModel, linkTo(methodOn(BookController.class)
                    .getBooksByAvailability(status)).withSelfRel());
        }
    }

    /**
     * In this method we return all books in the library by the expected price range.
     * @param maxprice -> The highest price allowed for a book.
     * @param minprice -> The lowest price allowed for a book.
     * @return All books in the library by the price range provided.
     */
    @Override
    public CollectionModel<EntityModel<BookDTO>> findBooksByPriceBetween(Double maxprice, Double minprice) {
        if(maxprice < minprice)
        {
            throw new LibraryNotFoundException((String.format(Messages.PRICE_CONFLICT_MESSAGE)));
        }
        List<Book> filteredBooks;
        if (minprice == null) {
            filteredBooks = bookRepository.findAll().stream()
                                          .filter(book -> book.getPrice() <= maxprice)
                                          .collect(Collectors.toList());
        } else {
            filteredBooks = bookRepository.findAll().stream()
                                          .filter(book -> book.getPrice() <= maxprice && book.getPrice() >= minprice)
                                          .collect(Collectors.toList());
        }

        List<BookDTO> bookDTOs = filteredBooks.stream()
                                              .map(BookDTO::new)
                                              .collect(Collectors.toList());

        CollectionModel<EntityModel<BookDTO>> bookDTOCollectionModel = bookDTOAssembler.toCollectionModel(bookDTOs);
        return CollectionModel.of(bookDTOCollectionModel, linkTo(methodOn(BookController.class)
                .findBooksByPriceBetween(maxprice, minprice)).withSelfRel());

    }

    /**
     * In this method delete a book from the library.
     * @param id -> id of the book.
     */
    @Override
    public void deleteBookById(UUID id) throws LibraryNotFoundException {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

                // Remove the book from the librarians before deleting it
            List<Librarian> librarians = book.getLibrarians();
            if (librarians != null) {
                librarians.forEach(librarian -> librarian.getBooks().remove(book));
                librarianRepository.saveAll(librarians);
            }


            bookRepository.delete(book);

            String successMessage = "Book deleted!";
            System.out.println(successMessage);
        } else {
            throw new LibraryNotFoundException((String.format(Messages.NOT_FOUND_MESSAGE, "book", id)));
        }
    }





    /**
     * In this method we create new Book.
     * @param book -> json with details about the book.
     * @return create new book in the database.
     */
    @Override
    public Book saveNewBook(Book book) {
        return bookRepository.save(book);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * In this method we update a book's price.
     * @param bookId -> id of the book in the library.
     * @param newPrice -> The new price.
     * @return update the book in the database.
     */
    @Override
    public BookDTO updateBookPrice(UUID bookId, Double newPrice) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.setPrice(newPrice);
        Book updatedBook = bookRepository.save(book);
        return new BookDTO(updatedBook);
    }


    /**
     * In this method we are adding to a customer's book list
                                        * a new book from the external library.
     * @param customerId -> id of the customer that accepts the book.
     * @param bookId -> id of the required book.
     * @return update the customer's book list in the database.
     */
    public void addBookToCustomerFromApi(String customerId, String bookId) throws ChangeSetPersister.NotFoundException {
        Customer customer = customerRepository.findById(Integer.valueOf(customerId))
                                              .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

        CompletableFuture<GoogleBookItem> bookFuture = googleBookService.getExternalBookById(bookId);
        GoogleBookItem googleBookItem = bookFuture.join(); // waiting for the CompletableFuture to complete

        Book book = new Book();
        book.setBookTitle(googleBookItem.getVolumeInfo().getTitle());
        book.setBookStatus(BookStatus.RESERVED);


        double titleLength = book.getBookTitle().length();
        double price = titleLength + 0.01 * (googleBookItem.getVolumeInfo().getPageCount());
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
                            // Sets the price based on the length of the book title and the amount of pages.
        book.setPrice(Double.valueOf(decimalFormat.format(price)));

        String publishedDate = googleBookItem.getVolumeInfo().getPublishedDate();
                     // Gets the full year out of the "yyyy-mm-dd" date format using substring() :
        if (publishedDate != null && publishedDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            int year = Integer.parseInt(publishedDate.substring(0, 4));
            book.setPublishDate(year);
        }

        customer.getBooks().add(book);
        book.setCustomer(customer);

        customerRepository.save(customer);
    }





            // Retrieves books without any specific filters
    @Override
    public CompletableFuture<ResponseEntity<List<Book>>> getAPIBooks(){
        CompletableFuture<List<Book>> booksFuture = googleBookService.getAPIBooks();

        return booksFuture.thenApply(ResponseEntity::ok)
                          .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
