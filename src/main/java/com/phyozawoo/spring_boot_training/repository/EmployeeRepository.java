package com.phyozawoo.spring_boot_training.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.phyozawoo.spring_boot_training.entity.Employee;
import com.phyozawoo.spring_boot_training.exception.EmployeeAlreadyExistsException;
import com.phyozawoo.spring_boot_training.exception.EmployeeNotFoundException;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public EmployeeRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Employee save(Employee employee){
        checkEmployeeExistsWithEmail(employee);
        dynamoDBMapper.save(employee);
        return employee;
    }

    public Employee getEmployeeById(String id){
        Employee employee = dynamoDBMapper.load(Employee.class,id);
        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }
        return employee;
    }

    public List<Employee> getEmployees(){
        return dynamoDBMapper.scan(Employee.class, new DynamoDBScanExpression());
    }

    public String deleteEmployee(String id){
        Employee employee = getEmployeeById(id);
        dynamoDBMapper.delete(employee);
        return "Successfully Deleted!";
    }

    public String updateEmployee(String id, Employee employee){
        getEmployeeById(id);
        checkEmployeeExistsWithEmail(employee);
        employee.setEmployeeId(id);

        // Conditional expression to ensure the item exists
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression()
                .withExpectedEntry("employee_id", new ExpectedAttributeValue(
                        new AttributeValue().withS(id)
                ));

        dynamoDBMapper.save(employee, saveExpression);
        return "Successfully Updated for the ID: " + id;
    }

    public Optional<Employee> getEmployeeByEmail(String email) {
        DynamoDBQueryExpression<Employee> queryExpression = new DynamoDBQueryExpression<Employee>()
                .withIndexName("email_index")  // Use the GSI
                .withConsistentRead(false)     // Must be false for GSI
                .withKeyConditionExpression("email = :emailVal")
                .withExpressionAttributeValues(Map.of(":emailVal", new AttributeValue().withS(email)));

        List<Employee> employees = dynamoDBMapper.query(Employee.class, queryExpression);

        return Optional.ofNullable(employees.isEmpty() ? null : employees.get(0));
    }


    public void checkEmployeeExistsWithEmail(Employee employee){
        Optional<Employee> optionalEmployee = getEmployeeByEmail(employee.getEmail());
        if(optionalEmployee.isPresent()){
            throw new EmployeeAlreadyExistsException("Employee already exits with given email!");
        }
    }
}
