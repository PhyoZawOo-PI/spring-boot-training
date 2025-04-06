package com.phyozawoo.spring_boot_training.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class Department {

    @Schema(
            example = "IT"
    )
    @DynamoDBAttribute
    @NotBlank(message = "Department name is mandatory")
    private String departmentName;
    @Schema(
            example = "001"
    )
    @DynamoDBAttribute
    @NotBlank(message = "Department code is mandatory")
    private String departmentCode;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
}
