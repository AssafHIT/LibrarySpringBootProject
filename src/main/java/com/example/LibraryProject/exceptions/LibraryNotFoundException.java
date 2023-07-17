package com.example.LibraryProject.exceptions;

public class LibraryNotFoundException extends RuntimeException{
    public LibraryNotFoundException(String message) {
        super(message);
    }
}
