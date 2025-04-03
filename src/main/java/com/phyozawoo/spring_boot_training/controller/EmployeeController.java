package com.phyozawoo.spring_boot_training.controller;

import com.phyozawoo.spring_boot_training.entity.Employee;
import com.phyozawoo.spring_boot_training.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "CRUD REST APIs for Employee",
        description = "CRUD REST APIs for Employee to CREATE,UPDATE,FETCH and DELETE employees"
)
@RestController
@RequestMapping("/employees")
public class EmployeeController {


    private  final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @Operation(
            summary = "Fetch Employee REST API",
            description = "REST API to fetch an Employee"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable  String id){
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @Operation(
            summary = "Fetch All Employee REST API",
            description = "REST API to fetch all Employees"
    )
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployee(){
        List<Employee> employeeList = employeeService.getAllEmployee();
        return ResponseEntity.ok(employeeList);
    }

    @Operation(
            summary = "Create Employee REST API",
            description = "REST API to create new Employee"
    )
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee){
        Employee savedEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(savedEmployee,HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete Employee REST API",
            description = "REST API to delete an Employee"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        String message = employeeService.deleteEmployee(id);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @Operation(
            summary = "Update Employee REST API",
            description = "REST API to update an Employee"
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id,
                                         @RequestBody Employee employee){
        String message = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(message);
    }
}
