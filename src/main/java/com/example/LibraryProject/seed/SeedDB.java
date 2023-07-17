package com.example.LibraryProject.seed;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.BookStatus;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.Librarian;
import com.example.LibraryProject.repository.BookRepository;
import com.example.LibraryProject.repository.CustomerRepository;
import com.example.LibraryProject.repository.LibrarianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration// על המחלקה להיות זמינה בזמן קומפלציה
public class SeedDB {
    private static final Logger logger = LoggerFactory.getLogger(SeedDB.class);
    @Bean// האחראי להריץ פקודות אלה הוא ספרינג
    CommandLineRunner initDatabase(BookRepository bookRepository, CustomerRepository customerRepository, LibrarianRepository librarianRepository) {
        return args -> {
            Librarian librarian = librarianRepository.save(new Librarian(21768989, UUID.randomUUID(), "Edna the Librarian", new ArrayList<>()));

            Book book1 = bookRepository.save(new Book( "Harry Potter and the Philosopher's Stone", BookStatus.RESERVED, 5.3, null, 1997));
            Book book2 = bookRepository.save(new Book( "Harry Potter and the Prisoner of Azkaban", BookStatus.AVAILABLE, 4.8, null, 1999));
            Book book3 = bookRepository.save(new Book( "Harry Potter and the Goblet of Fire", BookStatus.AVAILABLE, 8.5, null, 2000));
            Book book4 = bookRepository.save(new Book( "Harry Potter and the Order of the Phoenix", BookStatus.AVAILABLE, 6.9, null, 2003));
            Book book5 = bookRepository.save(new Book( "Harry Potter and the Half-Blood Prince", BookStatus.AVAILABLE, 7.4, null, 2005));
            Book book6 = bookRepository.save(new Book( "Harry Potter and the Deathly Hallows", BookStatus.AVAILABLE, 7.5, null, 2007));
            Book book7 = bookRepository.save(new Book( "Harry Potter and the Cursed Child", BookStatus.AVAILABLE, 9.6, null, 2016));

            List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6, book7);
            librarian.setBooks(books);
            librarianRepository.save(librarian);

            Customer assaf = customerRepository.save(new Customer(316040484, "Assaf Yehezkel", new ArrayList<Book>()));
            Customer benny = customerRepository.save(new Customer(205980857, "Benny Batash", new ArrayList<Book>()));

            List<Book> bookList = new ArrayList<>();
            bookList.add(book1);

            book1.setCustomer(assaf);
            assaf.setBooks(bookList);

            customerRepository.save(assaf);
        };
    }
}
