package com.example.LibraryProject.pojo.dto;

import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonPropertyOrder({"id","name", "books"})
public class CustomerDTO {
    private Integer customerId;
    private String name;
    private List<BookDTO> books;

    @JsonIgnore // להתעלם מהסדר המובנה של הלקוח
    private final Customer customer;
    public CustomerDTO(Customer customer) {
        this.customer = customer;
        this.customerId = customer.getPersonalId();
        this.name = customer.getName();
        this.books = customer.getBooks().stream().map(BookDTO::new).collect(Collectors.toList());
    }

    public Integer getCustomerId(){return this.customer.getPersonalId(); }

    public String getName()
    {
        return String.format("%s", this.customer.getName());
    }

    public List<BookDTO> getBooks() {
        return books;
    }
    public void setBooks(List<BookDTO> books) {
        this.books.addAll(books);
    }

}
