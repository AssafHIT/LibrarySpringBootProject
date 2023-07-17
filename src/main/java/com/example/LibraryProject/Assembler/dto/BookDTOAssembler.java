package com.example.LibraryProject.Assembler.dto;
import com.example.LibraryProject.Controller.BookController;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.dto.BookDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookDTOAssembler implements SimpleRepresentationModelAssembler<BookDTO> {
// // // המחלקה מייצרת Entity model של הDTO //

    private final ModelMapper modelMapper;
                            // Used to map the Book Object to a DTO; Needed for getBookById method in BookController.


    public BookDTOAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public BookDTO toDto(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public EntityModel<BookDTO> toModel(Book book) {
        BookDTO bookDTO = toDto(book);
        return EntityModel.of(bookDTO);
    }
    public CollectionModel<EntityModel<BookDTO>> toCollectionModel(List<Book> books) {
        List<EntityModel<BookDTO>> bookDTOs = books.stream()
                                                   .map(this::toModel)
                                                   .collect(Collectors.toList());

        return CollectionModel.of(bookDTOs);
    }

    ///////////////////////////// ADD LINKS  /////////////////////////////////////

    public void addLinks(EntityModel<BookDTO> resource) {
        try {
            resource.add(linkTo(methodOn(BookController.class)
                    .singleBookInfo(resource.getContent().getBookId())).withSelfRel());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }

        resource.add(linkTo(methodOn(BookController.class)
                .allBooksInfo())
                .withRel("All books information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<BookDTO>> resources) {
        resources.add(linkTo(methodOn(BookController.class).allBooksInfo()).withSelfRel());
    }
}