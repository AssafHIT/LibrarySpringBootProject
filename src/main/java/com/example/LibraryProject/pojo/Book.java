package com.example.LibraryProject.pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue
    private UUID id;
    private String bookTitle;

    @JsonIgnore
    private BookStatus bookStatus;

    private Double price;

    @JsonIgnore
    private Integer publishDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonIgnore
    @ManyToMany(mappedBy = "books")
    private List<Librarian> librarians;

    public String getBookTitle() {
        return bookTitle != null ? bookTitle : "";
    }


    public Book(String bookTitle, BookStatus bookStatus, Double price, Customer customer, Integer publishDate) {
        this.id = UUID.randomUUID();
        this.bookTitle = bookTitle;
        this.bookStatus = bookStatus;
        this.price = price;
        this.customer = customer;
        this.publishDate = publishDate;
    }



}
