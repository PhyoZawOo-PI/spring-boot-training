package com.phyozawoo.spring_boot_training.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmployeeAlreadyExistsException extends RuntimeException {

    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}

