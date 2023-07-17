package com.example.LibraryProject.Controller;
import com.example.LibraryProject.Assembler.CustomerEntityAssembler;
import com.example.LibraryProject.Assembler.dto.BookDTOAssembler;
import com.example.LibraryProject.Assembler.dto.CustomerDTOAssembler;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.CustomerDTO;
import com.example.LibraryProject.repository.BookRepository;
import com.example.LibraryProject.repository.CustomerRepository;
import com.example.LibraryProject.service.BookService;
import com.example.LibraryProject.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final BookDTOAssembler bookDTOAssembler;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    private final BookService bookService;
    private final BookRepository bookRepository;

    private final CustomerEntityAssembler customerEntityAssembler; // For converting pojos into Entity/Collection Models;
    private final CustomerDTOAssembler customerDTOAssembler; // For converting pojos into DTOs and returning answers to the users;


///////////     GET ALL CUSTOMERS    ///////////////////     http://localhost:8080/customers
    /**
     * In this method we get all the customers in our library.
     * @return stat us 200, all customers from the database.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> getAllCustomers(){
        return ResponseEntity.ok().body(customerService.getAllCustomers());


    }

    ////////////////////////  GET CUSTOMER BY ID  //  // //////////////////////
    /**
     * In this method we get a single customer by his personal ID.
     * @param customerId -> Expects to get the ID of the customer.
     * @return status 200, and the customer by his ID.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<EntityModel<Customer>> getCustomerById(@PathVariable Integer customerId) throws ChangeSetPersister.NotFoundException {
        EntityModel<Customer> customerModel = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customerModel);
    }

    ///////////     SINGLE CUSTOMER INFO    ///////////////    http://localhost:8080/customers/316040484/info
    /**
     * In this method we get DTO info about a single customer, by his ID.
     * @return status 200, and the customer DTO.
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<EntityModel<CustomerDTO>> singleCustomerInfo(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok().body(customerService.singleCustomerInfo(id));
    }

    ////////////////////////  ALL CUSTOMERS INFO ////////////////////// http://localhost:8080/customers/info
    /**
     * In this method we get DTOs info about all the customers in the library.
     * @return status 200, and the customer of the library.
     */
    @GetMapping("/info")
    public ResponseEntity<CollectionModel<EntityModel<CustomerDTO>>> allCustomersInfo() {
        return ResponseEntity.ok(customerDTOAssembler.toCollectionModel(
                StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                             .map(CustomerDTO::new).collect(Collectors.toList())));
    }

    //////////////        DELETE CUSTOMER BY ID    ////////////////////////////
    /**
     * In this method we delete a customer from the database.
     * @param customerId -> Expects to get the id of the customer that we want to delete.
     * @return status 202, delete the customer from database.
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable Integer customerId) throws ChangeSetPersister.NotFoundException {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

    //////////////     ADD NEW CUSTOMER    /////////////////////////////
    /**
     *  In this method we create a new customer in the database
     * @param customer -> Expects customer details.
     * @return status 201, create a new customer in the database.
     */
    @PostMapping
        public ResponseEntity<EntityModel<Customer>> addNewCustomer(@RequestBody Customer customer) {
            Customer savedCustomer = customerService.addNewCustomer(customer);
            EntityModel<Customer> customerModel = customerEntityAssembler.toModel(savedCustomer);
            return ResponseEntity.created(customerModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(customerModel);
        }


    //////////////////////     GET ALL CUSTOMER BOOKS    /////////////////////////////  http://localhost:8080/customers/316040484/books
    /**
     * In this method we get DTO info about the specific customer's books.
     * @return status 200, and the customer's book list.
     */
    @GetMapping("/{customerId}/books")
    public ResponseEntity<CollectionModel<EntityModel<BookDTO>>> getAllCustomerBooks(@PathVariable Integer customerId) throws ChangeSetPersister.NotFoundException {
        List<BookDTO> bookDTOs = customerService.getAllCustomerBooks(customerId);
        List<EntityModel<BookDTO>> bookEntities = bookDTOs.stream()
                                                          .map(bookDTOAssembler::toModel)
                                                          .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(bookEntities,
                linkTo(methodOn(CustomerController.class).getAllCustomerBooks(customerId)).withSelfRel()));
    }

    //////////////////////     UPDATE CUSTOMER'S BOOKS    /////////////////////////////  http://localhost:8080/customers/316040484/books
    @PutMapping("/{customerId}/books")
    public ResponseEntity<EntityModel<CustomerDTO>> updateCustomerBooks(
            @PathVariable Integer customerId,
            @RequestBody Book book) throws ChangeSetPersister.NotFoundException {
        Customer updatedCustomer = customerService.updateCustomerBookList(customerId, book);
        EntityModel<CustomerDTO> customerEntity = customerDTOAssembler.toModel(new CustomerDTO(updatedCustomer));
        return ResponseEntity.ok(customerEntity);
    }

    //////////////////////     UPDATE CUSTOMER'S NAME    /////////////////////////////  http://localhost:8080/customers/316040484
    /**
     * In this method we update the customer's name by his ID.
     * @param customerId -> Expects to get ID of the customer.
     * @param updatedCustomer -> Expects json with the new name.
     * @return status 201, updated new customer in the database.
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<EntityModel<Customer>> updateCustomersName(
            @PathVariable Integer customerId,
            @RequestBody Customer updatedCustomer) throws ChangeSetPersister.NotFoundException {
        Customer customer = customerService.updateCustomersName(customerId, updatedCustomer);
        EntityModel<Customer> customerModel = customerEntityAssembler.toModel(customer);
        return ResponseEntity.ok(customerModel);
    }

}


