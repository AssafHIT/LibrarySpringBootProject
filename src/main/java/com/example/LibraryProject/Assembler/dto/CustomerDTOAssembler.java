package com.example.LibraryProject.Assembler.dto;

import com.example.LibraryProject.Controller.BookController;
import com.example.LibraryProject.Controller.CustomerController;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.CustomerDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerDTOAssembler implements SimpleRepresentationModelAssembler<CustomerDTO> {
    // // // המחלקה מייצרת Entity model של הDTO //



    public void addLinks(EntityModel<CustomerDTO> resource) {
        try {
            resource.add(linkTo(methodOn(CustomerController.class)
                    .singleCustomerInfo(resource.getContent().getCustomerId())).withSelfRel());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }

        resource.add(linkTo(methodOn(CustomerController.class)
                .allCustomersInfo())
                .withRel("All customers information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<CustomerDTO>> resources) {
        resources.add(linkTo(methodOn(CustomerController.class).allCustomersInfo()).withSelfRel());
    }
}
