package com.example.LibraryProject.service;

import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import com.example.LibraryProject.pojo.dto.BookDTO;
import com.example.LibraryProject.pojo.dto.CustomerDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CustomerService {

    /////////////////////////// Get methods ///////////////////////////
    CollectionModel<EntityModel<Customer>> getAllCustomers();
    EntityModel<Customer> getCustomerById(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException;
    EntityModel<CustomerDTO> singleCustomerInfo(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException;
    CollectionModel<EntityModel<CustomerDTO>> allCustomersInfo();
    List<BookDTO> getAllCustomerBooks(Integer customerId) throws ChangeSetPersister.NotFoundException;
    /////////////////////////// Delete ///////////////////////////
    void deleteCustomerById(Integer id) throws ChangeSetPersister.NotFoundException;
    /////////////////////////// Post ///////////////////////////
    Customer addNewCustomer(Customer customer);
    /////////////////////////// Put ///////////////////////////
    Customer updateCustomerBookList(Integer customerId, Book book) throws ChangeSetPersister.NotFoundException;
    Customer updateCustomersName(Integer customerId, Customer updatedCustomer) throws ChangeSetPersister.NotFoundException;

}
