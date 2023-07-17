package com.example.LibraryProject.service;


import com.example.LibraryProject.pojo.Librarian;
import com.example.LibraryProject.pojo.dto.LibrarianDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.PathVariable;

public interface LibrarianService
{
    CollectionModel<EntityModel<Librarian>> getAllLibrarians();

    EntityModel<Librarian> getLibrarianById(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException;

    EntityModel<LibrarianDTO> singleLibrarianInfo(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException;

    Librarian addNewLibrarian(Librarian librarian);

    void deleteLibrarianById(Integer id) throws ChangeSetPersister.NotFoundException;


}

