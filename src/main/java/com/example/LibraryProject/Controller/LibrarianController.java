package com.example.LibraryProject.Controller;
import com.example.LibraryProject.Assembler.CustomerEntityAssembler;
import com.example.LibraryProject.Assembler.LibrarianEntityAssembler;
import com.example.LibraryProject.Assembler.dto.BookDTOAssembler;
import com.example.LibraryProject.Assembler.dto.CustomerDTOAssembler;
import com.example.LibraryProject.Assembler.dto.LibrarianDTOAssembler;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.Librarian;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.CustomerDTO;
import com.example.LibraryProject.pojo.dto.LibrarianDTO;
import com.example.LibraryProject.repository.CustomerRepository;
import com.example.LibraryProject.repository.LibrarianRepository;
import com.example.LibraryProject.service.LibrarianService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
@RequestMapping("/librarians")
public class LibrarianController {
    private final LibrarianRepository librarianRepository;
    private final LibrarianService librarianService;
    private final LibrarianEntityAssembler librarianEntityAssembler;
    private final LibrarianDTOAssembler librarianDTOAssembler;
    private final BookDTOAssembler bookDTOAssembler;



///////////     GET ALL LIBRARIANS    ///////////////////     http://localhost:8080/librarians
    /**
     * In this method we get all the librarians in our library.
     * @return stat us 200, all librarians from the database.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Librarian>>> getAllLibrarians() {
        return ResponseEntity.ok().body(librarianService.getAllLibrarians());
    }

    ////////////////////////  GET LIBRARIAN BY ID  //  // //////////////////////
    /**
     * In this method we get a single librarian by her personal ID.
     * @param librarianId -> Expects to get the ID of the librarian.
     * @return status 200, and the librarian by her ID.
     */
    @GetMapping("/{librarianId}")
    public ResponseEntity<EntityModel<Librarian>> getLibrarianById(@PathVariable Integer librarianId) throws ChangeSetPersister.NotFoundException {
        EntityModel<Librarian> LibrarianModel = librarianService.getLibrarianById(librarianId);
        return ResponseEntity.ok(LibrarianModel);
    }

    ///////////     SINGLE LIBRARIAN INFO    ///////////////    http://localhost:8080/librarians/3214587/info
    /**
     * In this method we get DTO info about a single librarian, by her ID.
     * @return status 200, and the librarian DTO.
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<EntityModel<LibrarianDTO>> singleLibrarianInfo(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok().body(librarianService.singleLibrarianInfo(id));
    }



    ////////////////////////  ALL LIBRARIANS INFO ////////////////////// http://localhost:8080/librarians/info
    /**
     * In this method we get DTOs info about all the librarians in the library.
     * @return status 200, and the librarians from the library.
     */
    @GetMapping("/info")
    public ResponseEntity<CollectionModel<EntityModel<LibrarianDTO>>> allLibrariansInfo() {
        return ResponseEntity.ok(librarianDTOAssembler.toCollectionModel(
                StreamSupport.stream(librarianRepository.findAll().spliterator(), false)
                             .map(LibrarianDTO::new).collect(Collectors.toList())));

}


    //////////////        DELETE LIBRARIAN BY ID    ////////////////////////////
    /**
     * In this method we delete a librarian from the database.
     * @param librarianId -> Expects to get the id of the librarian that we want to delete.
     * @return status 202, delete the librarian from database.
     */
    @DeleteMapping("/{librarianId}")
    public ResponseEntity<?> deleteLibrarianById(@PathVariable Integer librarianId) throws ChangeSetPersister.NotFoundException {
        librarianService.deleteLibrarianById(librarianId);
        return ResponseEntity.noContent().build();
    }

    //////////////     ADD NEW LIBRARIAN    /////////////////////////////
    /**
     *  In this method we create a new librarian in the database
     * @param librarian -> Expects librarian details.
     * @return status 201, create a new librarian in the database.
     */
    @PostMapping
    public ResponseEntity<EntityModel<Librarian>> addNewLibrarian(@RequestBody Librarian librarian) {
        Librarian savedLibrarian = librarianService.addNewLibrarian(librarian);
        EntityModel<Librarian> librarianModel = librarianEntityAssembler.toModel(savedLibrarian);
        return ResponseEntity.created(librarianModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(librarianModel);
    }

}
