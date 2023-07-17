package com.example.LibraryProject.service;
import com.example.LibraryProject.Assembler.LibrarianEntityAssembler;
import com.example.LibraryProject.Assembler.dto.LibrarianDTOAssembler;
import com.example.LibraryProject.Controller.LibrarianController;
import com.example.LibraryProject.exceptions.LibraryNotFoundException;
import com.example.LibraryProject.exceptions.Messages;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.Librarian;
import com.example.LibraryProject.pojo.dto.LibrarianDTO;
import com.example.LibraryProject.repository.BookRepository;
import com.example.LibraryProject.repository.LibrarianRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LibrarianServiceImpl implements LibrarianService {
    private final BookRepository bookRepository;
    private final LibrarianEntityAssembler librarianEntityAssembler;
    private final LibrarianRepository librarianRepository;
    private final LibrarianDTOAssembler librarianDTOAssembler;

    public LibrarianServiceImpl(BookRepository bookRepository, LibrarianRepository librarianRepository,
                                LibrarianEntityAssembler librarianEntityAssembler, LibrarianDTOAssembler librarianDTOAssembler) {
        this.bookRepository = bookRepository;
        this.librarianRepository = librarianRepository;
        this.librarianEntityAssembler = librarianEntityAssembler;
        this.librarianDTOAssembler = librarianDTOAssembler;
    }

    /**
     *  In this method we return all the librarians in the library.
     * @return Librarians
     */
    @Override
    public CollectionModel<EntityModel<Librarian>> getAllLibrarians() {
        List<EntityModel<Librarian>> librarian = librarianRepository.findAll().stream().map
                (librarianEntityAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(librarian, linkTo(methodOn(LibrarianController.class)
                .getAllLibrarians()).withSelfRel());
    }

    /**
     * In this method we return a single librarian from the library by the ID.
     * @param id -> Personal id of the librarian.
     * @return single librarian.
     */
    @Override
    public EntityModel<Librarian> getLibrarianById(Integer id) throws ChangeSetPersister.NotFoundException {
        Librarian librarian = librarianRepository.findById(id)
                                              .orElseThrow(() ->
                                                      new LibraryNotFoundException(
                                                              (String.format(Messages.LIBRARIAN_ERROR_MESSAGE, id))));

        return librarianEntityAssembler.toModel(librarian);
    }

    /**
     * In this method we return a librarian DTO from the library by the ID.
     * @param id -> personal ID of the librarian.
     * @return single librarian DTO.
     */
    @Override
    public EntityModel<LibrarianDTO> singleLibrarianInfo(Integer id) throws ChangeSetPersister.NotFoundException {
        return librarianRepository.findById(id)
                                 .map(LibrarianDTO::new)
                                 .map(librarianDTOAssembler::toModel)
                                 .orElseThrow(() ->
                                         new LibraryNotFoundException(
                                                 (String.format(Messages.LIBRARIAN_ERROR_MESSAGE, id))));
    }

    /**
     * In this method we create new librarian.
     * @param librarian -> json with details about the librarian.
     * @return create new librarian in the database.
     */
    @Override
    public Librarian addNewLibrarian(Librarian librarian) {

        return librarianRepository.save(librarian);
    }

    /**
     * In this method delete a librarian from the library.
     * @param id -> Personal id of the librarian.
     */
    @Override
    public void deleteLibrarianById(Integer id) throws ChangeSetPersister.NotFoundException {
        Librarian librarian = librarianRepository.findById(id)
                                              .orElseThrow(() ->
                                                      new LibraryNotFoundException(
                                                              (String.format(Messages.NOT_FOUND_MESSAGE, "librarian", id))));

        librarianRepository.delete(librarian);
    }
}