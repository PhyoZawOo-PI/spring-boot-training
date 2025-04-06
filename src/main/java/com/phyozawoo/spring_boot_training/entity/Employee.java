package com.phyozawoo.spring_boot_training.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "employee")
public class Employee {
    @Schema(
            example = "aheaw89yr9q3i20iq0eq"
    )
    @DynamoDBHashKey(attributeName = "employee_id")
    @DynamoDBAutoGeneratedKey
    private String employeeId;
    @Schema(
            example = "Phyo Zaw"
    )
    @DynamoDBAttribute(attributeName = "first_name")
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @Schema(
            example = "Oo"
    )
    @DynamoDBAttribute(attributeName = "last_name")
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @Schema(
            example = "aheaw89yr9q3i20iq0eq"
    )
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "email_index")
    @NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid!")
    private String email;
    @DynamoDBAttribute
    @Valid // 👈 This tells the validator to validate nested fields inside `Department`
    @NotNull(message="Department is mandatory")
    private Department department;

    public Employee(String firstName, String lastName, String email, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
