package com.example.LibraryProject.Assembler;


import com.example.LibraryProject.Controller.BookController;
import com.example.LibraryProject.pojo.Book;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookEntityAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {
// // // המחלקה עוזרת בתהליך של לקיחת אוביקט ולהפוך אותו לקונטיינר של אובייקטים בתוספת לינקים //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public EntityModel<Book> toModel(Book entity) {
        return EntityModel.of(entity, linkTo(methodOn(BookController.class)
                .getBookById(entity.getId())).withSelfRel(), linkTo(methodOn(BookController.class)
                .getBooks()).withRel("All Books"));
    }

    @Override
    public CollectionModel<EntityModel<Book>> toCollectionModel(Iterable<? extends Book> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
