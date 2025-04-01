package com.phyozawoo.spring_boot_training.service;

import com.phyozawoo.spring_boot_training.entity.Employee;
import com.phyozawoo.spring_boot_training.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable String id){
        return employeeRepository.getEmployeeById(id);
    }

    @GetMapping
    public List<Employee> getAllEmployee(){
        return employeeRepository.getEmployees();
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable String id){
        return employeeRepository.deleteEmployee(id);
    }

    @PutMapping("/{id}")
    public String updateEmployee(@PathVariable String id,
                                         @RequestBody Employee employee){
        return employeeRepository.updateEmployee(id, employee);
    }

}
