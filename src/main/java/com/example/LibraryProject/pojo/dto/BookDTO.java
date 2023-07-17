package com.example.LibraryProject.pojo.dto;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"intro", "title", "price", "status", "customer"})
public class BookDTO {
    @JsonIgnore
    private Book book;

    public BookDTO(String title, String[] authors, Double price) {
    }

    @JsonIgnore
    public UUID getBookId() {
        return book.getId();
    }

   public String getName() {
       return String.format("%s", book.getBookTitle());
   }


    public Double getPrice() {
        return book.getPrice();
    }

    public String getIntro(){return "Presenting book: " + book.getBookTitle().toUpperCase();}

    public String getBookStatus() {
        return String.format("%s", book.getBookStatus());
    }

    public Integer getPublishDate(){return book.getPublishDate();}

    public BookDTO(Book book) {
        this.book = book;

    }


}
