package com.example.LibraryProject.service;

import com.example.LibraryProject.API.GoogleBookService;
import com.example.LibraryProject.Assembler.CustomerEntityAssembler;
import com.example.LibraryProject.Assembler.dto.CustomerDTOAssembler;
import com.example.LibraryProject.Controller.CustomerController;
import com.example.LibraryProject.exceptions.LibraryNotFoundException;
import com.example.LibraryProject.exceptions.Messages;
import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.CustomerDTO;
import com.example.LibraryProject.repository.BookRepository;
import com.example.LibraryProject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final BookRepository bookRepository;
    private final CustomerEntityAssembler customerEntityAssembler;
    private final CustomerRepository customerRepository;
    private final CustomerDTOAssembler customerDTOAssembler;
    private final GoogleBookService googleBookService;

    /**
     *  In this method we return all the customers in the library.
     * @return Customers
     */
    @Override
    public CollectionModel<EntityModel<Customer>> getAllCustomers() {
        List<EntityModel<Customer>> customers = customerRepository.findAll().stream().map
                (customerEntityAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class)
                .getAllCustomers()).withSelfRel());
    }


    /**
     * In this method we return a single customer from the library by the ID.
     * @param id -> Personal id of the customer.
     * @return single customer.
     */
    @Override
    public EntityModel<Customer> getCustomerById(Integer id) throws LibraryNotFoundException {
        Customer customer = customerRepository.findById(id)
                                              .orElseThrow(() ->
                           new LibraryNotFoundException(
                                   (String.format(Messages.CUSTOMER_ERROR_MESSAGE, id))));

        return customerEntityAssembler.toModel(customer);
    }

    /**
     * In this method we return a customer DTO from the library by the ID.
     * @param id -> personal ID of the customer.
     * @return single customer DTO.
     */
    @Override
    public EntityModel<CustomerDTO> singleCustomerInfo(Integer id) throws ChangeSetPersister.NotFoundException {
        return customerRepository.findById(id)
                             .map(CustomerDTO::new)
                             .map(customerDTOAssembler::toModel)
                             .orElseThrow(() ->
                                     new LibraryNotFoundException(
                                             (String.format(Messages.CUSTOMER_ERROR_MESSAGE, id))));

    }

    /**
     * In this method we return all customers DTO from the library.
     * @return Customers DTOs.
     */
    @Override
    public CollectionModel<EntityModel<CustomerDTO>> allCustomersInfo() {
        List<EntityModel<CustomerDTO>> customerDTOs = customerRepository.findAll().stream()
                                                                        .map(CustomerDTO::new)
                                                                        .map(customerDTOAssembler::toModel)
                                                                        .collect(Collectors.toList());

        return CollectionModel.of(customerDTOs,
                linkTo(methodOn(CustomerController.class).allCustomersInfo()).withSelfRel());
    }


    /**
     * In this method delete a customer.
     * @param id -> Personal id of the customer.
     */
    @Override
    public void deleteCustomerById(Integer id) throws ChangeSetPersister.NotFoundException {
        Customer customer = customerRepository.findById(id)
                                              .orElseThrow(() ->
                                                      new LibraryNotFoundException(
                                                              (String.format(Messages.NOT_FOUND_MESSAGE, "customer", id))));

        customerRepository.delete(customer);
    }

    /**
     * In this method we create new customer.
     * @param customer -> json with details about the customer.
     * @return create new customer in the database.
     */
    @Override
    public Customer addNewCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomerBookList(Integer customerId, Book book) throws ChangeSetPersister.NotFoundException {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

        // Add the book to the customer's list of books
        book.setCustomer(customer);
        customer.getBooks().add(book);

        // Update the customer in the repository
        return customerRepository.save(customer);
    }



    /**
     * In this method we update the customer's name.
     * @param customerId -> Personal id of the customer.
     * @param updatedCustomer -> json with the customer's name.
     * @return update the customer name field.
     */
    @Override
    public Customer updateCustomersName(Integer customerId, Customer updatedCustomer) throws ChangeSetPersister.NotFoundException {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new LibraryNotFoundException(
                                                      (String.format(Messages.CUSTOMER_ERROR_MESSAGE, customerId))));


        customer.setName(updatedCustomer.getName());
        return customerRepository.save(customer);
    }



    /**
     * In this method we return all customers books DTO from the library.
     * @param customerId -> Personal id of the customer.
     * @return Customers books DTOs.
     */
    @Override
    public List<BookDTO> getAllCustomerBooks(Integer customerId) throws ChangeSetPersister.NotFoundException {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new LibraryNotFoundException(
                                                      (String.format(Messages.CUSTOMER_ERROR_MESSAGE, customerId))));

        return customer.getBooks().stream()
                       .map(BookDTO::new)
                       .collect(Collectors.toList());
    }









}
