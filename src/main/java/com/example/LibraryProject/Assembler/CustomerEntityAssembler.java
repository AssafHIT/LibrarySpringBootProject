package com.example.LibraryProject.Assembler;

import com.example.LibraryProject.Controller.BookController;
import com.example.LibraryProject.Controller.CustomerController;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.CustomerDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerEntityAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {
    // // // המחלקה עוזרת בתהליך של לקחת אוביקט ולהפוך אותו לקונטיינר של אובייקטים בתוספת לינקים //



    @Override
    public EntityModel<Customer> toModel(Customer entity) {
        try {
            return EntityModel.of(entity, linkTo(methodOn(CustomerController.class)
                    .getCustomerById(entity.getPersonalId())).withSelfRel(), linkTo(methodOn(CustomerController.class)
                    .getAllCustomers()).withRel("All Customers"));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CollectionModel<EntityModel<Customer>> toCollectionModel(Iterable<? extends Customer> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
