package com.example.LibraryProject.repository;
import com.example.LibraryProject.pojo.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LibrarianRepository extends JpaRepository<Librarian, Integer> {


}
