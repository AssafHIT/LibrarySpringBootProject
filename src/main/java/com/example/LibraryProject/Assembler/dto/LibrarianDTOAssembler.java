package com.example.LibraryProject.Assembler.dto;

import com.example.LibraryProject.Controller.CustomerController;
import com.example.LibraryProject.Controller.LibrarianController;
import com.example.LibraryProject.pojo.dto.CustomerDTO;
import com.example.LibraryProject.pojo.dto.LibrarianDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LibrarianDTOAssembler implements SimpleRepresentationModelAssembler<LibrarianDTO> {
    public void addLinks(EntityModel<LibrarianDTO> resource) {
        try {
            resource.add(linkTo(methodOn(CustomerController.class)
                    .singleCustomerInfo(resource.getContent().getLibrarianId())).withSelfRel());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }

        resource.add(linkTo(methodOn(LibrarianController.class)
                .allLibrariansInfo())
                .withRel("All librarian information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<LibrarianDTO>> resources) {
        resources.add(linkTo(methodOn(LibrarianController.class).allLibrariansInfo()).withSelfRel());
    }
}
