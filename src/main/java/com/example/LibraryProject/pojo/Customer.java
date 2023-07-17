package com.example.LibraryProject.pojo;

import com.example.LibraryProject.pojo.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
    @Id
    private Integer personalId;
    private String name;

    @JsonIgnore @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Book> books;
    //@OneToMany(mappedBy = "customer")
    //private List<Book> books = new ArrayList<>();
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        if (this.books == null) {
            this.books = new ArrayList<>();
        }
        this.books.addAll(books);
    }

    @JsonIgnore
    public List<BookDTO> getBookDTOs() {
        return books.stream()
                    .map(BookDTO::new)
                    .collect(Collectors.toList());
    }
}
