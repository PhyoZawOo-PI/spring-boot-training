package com.phyozawoo.spring_boot_training;

import com.phyozawoo.spring_boot_training.entity.Department;
import com.phyozawoo.spring_boot_training.entity.Employee;
import com.phyozawoo.spring_boot_training.exception.EmployeeAlreadyExistsException;
import com.phyozawoo.spring_boot_training.exception.EmployeeNotFoundException;
import com.phyozawoo.spring_boot_training.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpringBootTrainingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private EmployeeRepository employeeRepository;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void shouldReturnEmployee_WhenIdIsValid() throws Exception {
		Employee employee = new Employee("001", "Phyo Zaw", "Oo", "phyozawoo@gmail.com", new Department("IT","DEP001"));
		when(employeeRepository.getEmployeeById("001")).thenReturn(employee);

		mockMvc.perform(get("/employees/001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.employeeId").value("001"))
				.andExpect(jsonPath("$.firstName").value("Phyo Zaw"))
				.andExpect(jsonPath("$.lastName").value("Oo"))
				.andExpect(jsonPath("$.email").value("phyozawoo@gmail.com"))
				.andExpect(jsonPath("$.department.departmentName").value("IT")) // Verify department name
				.andExpect(jsonPath("$.department.departmentCode").value("DEP001"));
	}

	@Test
	void shouldReturn404_WhenEmployeeNotFound() throws Exception {
		when(employeeRepository.getEmployeeById("999")).thenThrow(new EmployeeNotFoundException("999"));

		mockMvc.perform(get("/employees/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.apiPath").value("uri=/employees/999"))
				.andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
				.andExpect(jsonPath("$.errorMessage").value("Employee not found with the given id : 999"));

	}

	@Test
	void shouldReturn400_WhenIdIsEmpty() throws Exception {
		mockMvc.perform(get("/employees/"))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldReturn500_WhenDatabaseErrorOccurs() throws Exception {
		when(employeeRepository.getEmployeeById("123")).thenThrow(new RuntimeException("Database error"));

		mockMvc.perform(get("/employees/123"))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.errorMessage").value("Database error"));
	}

	@Test
	void shouldReturnListOfEmployees() throws Exception {
		// ✅ Create mock employee list
		List<Employee> employees = List.of(
				new Employee("001", "Phyo Zaw", "Oo", "phyozawoo@gmail.com", new Department("IT", "DEP001")),
				new Employee("002", "John", "Doe", "johndoe@gmail.com", new Department("HR", "DEP002"))
		);

		// ✅ Mock service layer response
		when(employeeRepository.getEmployees()).thenReturn(employees);

		// ✅ Perform API request and verify response
		mockMvc.perform(get("/employees"))
				.andExpect(status().isOk()) // ✅ Check HTTP 200 OK
				.andExpect(jsonPath("$.length()").value(2)) // ✅ Ensure 2 employees are returned
				.andExpect(jsonPath("$[0].employeeId").value("001"))
				.andExpect(jsonPath("$[0].firstName").value("Phyo Zaw"))
				.andExpect(jsonPath("$[0].lastName").value("Oo"))
				.andExpect(jsonPath("$[0].email").value("phyozawoo@gmail.com"))
				.andExpect(jsonPath("$[0].department.departmentName").value("IT"))
				.andExpect(jsonPath("$[0].department.departmentCode").value("DEP001"))
				.andExpect(jsonPath("$[1].employeeId").value("002"))
				.andExpect(jsonPath("$[1].firstName").value("John"))
				.andExpect(jsonPath("$[1].lastName").value("Doe"))
				.andExpect(jsonPath("$[1].email").value("johndoe@gmail.com"))
				.andExpect(jsonPath("$[1].department.departmentName").value("HR"))
				.andExpect(jsonPath("$[1].department.departmentCode").value("DEP002"));
	}

	@Test
	void shouldCreateEmployee_WhenValidRequest() throws Exception {
		// ✅ Create mock employee data
		Employee employee = new Employee("001", "Phyo Zaw", "Oo", "phyozawoo@gmail.com", new Department("IT", "DEP001"));

		// ✅ Mock service layer response
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

		// ✅ Perform API request and verify response
		mockMvc.perform(post("/employees")
						.contentType(MediaType.APPLICATION_JSON) // ✅ Send JSON
						.content(objectMapper.writeValueAsString(employee))) // ✅ Convert Java object to JSON
				.andExpect(status().isCreated()) // ✅ Check HTTP 201 Created
				.andExpect(jsonPath("$.employeeId").value("001"))
				.andExpect(jsonPath("$.firstName").value("Phyo Zaw"))
				.andExpect(jsonPath("$.lastName").value("Oo"))
				.andExpect(jsonPath("$.email").value("phyozawoo@gmail.com"))
				.andExpect(jsonPath("$.department.departmentName").value("IT"))
				.andExpect(jsonPath("$.department.departmentCode").value("DEP001"));
	}

	@Test
	void shouldReturnBadRequest_WhenRequiredFieldsAreMissing() throws Exception {
		// Create an employee object with missing fields
		Employee invalidEmployee = new Employee( null, null, "invalid_email", null);

		// Perform API request with missing fields and verify response
		mockMvc.perform(post("/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidEmployee)))
				.andExpect(status().isBadRequest()) // Expect 400 Bad Request
				.andExpect(jsonPath("$.errors").exists()) // Verify that 'errors' field exists
				.andExpect(jsonPath("$.errors.firstName").value("First name is mandatory")) // Validate specific error message for 'firstName'
				.andExpect(jsonPath("$.errors.lastName").value("Last name is mandatory")) // Validate specific error message for 'firstName'
				.andExpect(jsonPath("$.errors.email").value("Email should be valid!")) // Validate specific error message for 'email'
				.andExpect(jsonPath("$.errors.department").value("Department is mandatory")); // Validate specific error message for 'department'
	}

	@Test
	void shouldReturnBadRequest_WhenEmployeeAlreadyExists() throws Exception {
		Employee employee = new Employee("001", "Phyo", "Oo", "phyo@gmail.com", new Department("IT", "DEP001"));

		when(employeeRepository.save(any(Employee.class)))
				.thenThrow(new EmployeeAlreadyExistsException("Employee already exists"));

		mockMvc.perform(post("/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(employee)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage").value("Employee already exists"));
	}

	@Test
	void shouldDeleteEmployee_WhenIdIsValid() throws Exception {
		String id = "001";
		String successMessage = "Successfully Deleted!";

		when(employeeRepository.deleteEmployee(id)).thenReturn(successMessage);

		mockMvc.perform(delete("/employees/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().string(successMessage));
	}

	@Test
	void shouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
		String id = "999";

		when(employeeRepository.deleteEmployee(id))
				.thenThrow(new EmployeeNotFoundException(id));

		mockMvc.perform(delete("/employees/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorMessage").value(String.format("Employee not found with the given id : %s",id)));
	}

	@Test
	void shouldUpdateEmployee_WhenRequestIsValid() throws Exception {
		Employee employee = new Employee("001", "Phyo", "Zaw", "phyo@gmail.com", new Department("IT", "DEP001"));

		when(employeeRepository.updateEmployee(eq("001"), any(Employee.class)))
				.thenReturn("Successfully Updated for the ID: 001");

		mockMvc.perform(put("/employees/001")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(employee)))
				.andExpect(status().isOk())
				.andExpect(content().string("Successfully Updated for the ID: 001"));
	}

	@Test
	void shouldReturnNotFoundWhenUpdate_WhenEmployeeDoesNotExist() throws Exception {
		Employee employee = new Employee("999", "Missing", "User", "missing@gmail.com", new Department("IT", "DEP001"));

		when(employeeRepository.updateEmployee(eq("999"), any(Employee.class)))
				.thenThrow(new EmployeeNotFoundException(employee.getEmployeeId()));

		mockMvc.perform(put("/employees/{id}",employee.getEmployeeId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(employee)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorMessage").value((String.format("Employee not found with the given id : %s",employee.getEmployeeId()))));
	}

	@Test
	void shouldReturnBadRequestWhenUpdate_WhenRequiredFieldsAreMissing() throws Exception {
		Employee existingEmployee = new Employee("001", "Old", "Name", "old@gmail.com", new Department("HR", "DEP999"));

		// Mocking repository to simulate "employee exists"
		when(employeeRepository.getEmployeeById("001")).thenReturn(existingEmployee);
		// Create an employee object with missing fields
		Employee invalidEmployee = new Employee( "001",null, null, "invalid_email", null);

		// Perform API request with missing fields and verify response
		mockMvc.perform(put("/employees/{id}",invalidEmployee.getEmployeeId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidEmployee)))
				.andExpect(status().isBadRequest()) // Expect 400 Bad Request
				.andExpect(jsonPath("$.errors").exists()) // Verify that 'errors' field exists
				.andExpect(jsonPath("$.errors.firstName").value("First name is mandatory")) // Validate specific error message for 'firstName'
				.andExpect(jsonPath("$.errors.lastName").value("Last name is mandatory")) // Validate specific error message for 'firstName'
				.andExpect(jsonPath("$.errors.email").value("Email should be valid!")) // Validate specific error message for 'email'
				.andExpect(jsonPath("$.errors.department").value("Department is mandatory")); // Validate specific error message for 'department'
	}

}
