package com.phyozawoo.spring_boot_training.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.phyozawoo.spring_boot_training.entity.Employee;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public EmployeeRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Employee save(Employee employee){
        dynamoDBMapper.save(employee);
        return employee;
    }

    public Employee getEmployeeById(String id){
        return  dynamoDBMapper.load(Employee.class,id);
    }

    public List<Employee> getEmployees(){
        return dynamoDBMapper.scan(Employee.class, new DynamoDBScanExpression());
    }

    public String deleteEmployee(String id){
        Employee employee = dynamoDBMapper.load(Employee.class, id);
        dynamoDBMapper.delete(employee);
        return "Successfully Deleted!";
    }

    public String updateEmployee(String id, Employee employee){
        employee.setEmployeeId(id);

        // Conditional expression to ensure the item exists
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression()
                .withExpectedEntry("employee_id", new ExpectedAttributeValue(
                        new AttributeValue().withS(id)
                ));

        dynamoDBMapper.save(employee, saveExpression);
        return "Successfully Updated for the ID: " + id;
    }
}
