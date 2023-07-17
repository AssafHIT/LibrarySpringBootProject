package com.example.LibraryProject.pojo;

import com.example.LibraryProject.pojo.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Librarian
{
    @Id
    private Integer personalId;

    @GeneratedValue(generator = "UUID")
    private final UUID workId;

    private String name;

    public Librarian() {
        this.workId = UUID.randomUUID();

    }

    @JsonIgnore
    @ManyToMany
    private List<Book> books;

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
