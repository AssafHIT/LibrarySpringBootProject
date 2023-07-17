package com.example.LibraryProject.Assembler;


import com.example.LibraryProject.Controller.CustomerController;
import com.example.LibraryProject.Controller.LibrarianController;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.Librarian;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LibrarianEntityAssembler implements RepresentationModelAssembler<Librarian, EntityModel<Librarian>> {
    @Override
    public EntityModel<Librarian> toModel(Librarian entity) {
        try {
            return EntityModel.of(entity, linkTo(methodOn(LibrarianController.class)
                    .getLibrarianById(entity.getPersonalId())).withSelfRel(), linkTo(methodOn(LibrarianController.class)
                    .getAllLibrarians()).withRel("All Librarians"));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CollectionModel<EntityModel<Librarian>> toCollectionModel(Iterable<? extends Librarian> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}