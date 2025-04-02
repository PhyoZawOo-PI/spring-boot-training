package com.phyozawoo.spring_boot_training.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmployeeNotFoundException extends RuntimeException{

    public EmployeeNotFoundException(String employeeId){
        super(String.format("Employee not found with the given id : %s", employeeId));
    }

}