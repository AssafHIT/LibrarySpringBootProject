package com.example.LibraryProject.repository;


import com.example.LibraryProject.pojo.Book;
import com.example.LibraryProject.pojo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository  extends JpaRepository<Customer, Integer> {



}
