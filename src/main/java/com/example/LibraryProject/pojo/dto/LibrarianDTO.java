package com.example.LibraryProject.pojo.dto;


import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.Librarian;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@JsonPropertyOrder({"id","name", "books"})
public class LibrarianDTO {
    @JsonIgnore
    private final Librarian librarian;
    private List<BookDTO> books;
    private Integer librarianId;
    private String name;

    public LibrarianDTO(Librarian librarian) {
        this.librarian = librarian;
        this.librarianId = librarian.getPersonalId();
        this.name = librarian.getName();
        this.books = librarian.getBooks().stream().map(BookDTO::new).collect(Collectors.toList());
    }
    public Librarian getLibrarian(){return this.librarian;}

    public String getName()
    {
        return String.format("%s", this.librarian.getName());
    }
    public Integer getLibrarianId(){return this.librarian.getPersonalId(); }


    public List<BookDTO> getBooks() {
        return books;
    }
    public void setBooks(List<BookDTO> books) {
        this.books.addAll(books);
    }

}
