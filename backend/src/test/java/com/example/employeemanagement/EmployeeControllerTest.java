package com.example.employeemanagement;

import com.example.employeemanagement.controller.EmployeeController;
import com.example.employeemanagement.exception.GlobalExceptionHandler;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // Positive Test Case: Verifies that adding a new employee is handled correctly
    @Test
    public void testAddEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");

        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"department\": \"IT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    // Positive Test Case: Verifies that getting an employee by ID is handled correctly
    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        when(employeeService.getEmployeeById(anyLong())).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    // Positive Test Case: Verifies that getting all employees is handled correctly
    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Doe");

        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    // Positive Test Case: Verifies that deleting an employee is handled correctly
    @Test
    public void testDeleteEmployee() throws Exception {
        when(employeeService.deleteEmployee(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // Negative Test Case: Verifies that getting an employee by ID returns 404 when the employee is not found
    @Test
    public void testGetEmployeeById_Negative() throws Exception {
        when(employeeService.getEmployeeById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Positive Test Case: Verifies that updating an existing employee is handled correctly
    @Test
    public void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setFirstName("John");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("john.doe@example.com");
        updatedEmployee.setDepartment("IT");

        when(employeeService.updateEmployee(anyLong(), any(Employee.class))).thenReturn(Optional.of(updatedEmployee));

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"department\": \"IT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }


    // Negative Test Case: Verifies that deleting an employee returns 404 when the employee is not found
    @Test
    public void testDeleteEmployee_Negative() throws Exception {
        when(employeeService.deleteEmployee(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Negative Test Case: Verifies that deleting an already deleted employee returns 404
    @Test
    public void testDeleteAlreadyDeletedEmployee() throws Exception {
        when(employeeService.deleteEmployee(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Negative Test Case: Verifies that updating a non-existing employee returns 404
    @Test
    public void testUpdateNonExistingEmployee() throws Exception {
        when(employeeService.updateEmployee(anyLong(), any(Employee.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"department\": \"IT\"}"))
                .andExpect(status().isNotFound());
    }

    // Negative Test Case: Verifies that adding an employee with duplicate email returns 409 conflict
    @Test
    public void testAddEmployeeWithDuplicateEmail() throws Exception {
        when(employeeService.saveEmployee(any(Employee.class))).thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"department\": \"IT\"}"))
                .andExpect(status().isConflict());
    }


    // Negative Test Case: Verifies that getting an employee by invalid ID returns 400 bad request
    @Test
    public void testGetEmployeeWithInvalidId() throws Exception {
        mockMvc.perform(get("/api/employees/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Negative Test Case: Verifies that deleting an employee by invalid ID returns 400 bad request
    @Test
    public void testDeleteEmployeeWithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/employees/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    // Edge Test Case: Verifies that updating an employee with null values is handled correctly
    @Test
    public void testUpdateEmployeeWithNullValues() throws Exception {
        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": null, \"lastName\": null, \"email\": null, \"department\": null}"))
                .andExpect(status().isBadRequest());
    }

    // Edge Test Case: Verifies that adding an employee with missing fields is handled correctly
    @Test
    public void testAddEmployeeWithMissingFields() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"\", \"lastName\": \"\", \"email\": \"\", \"department\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    // Edge Test Case: Verifies that getting employees when the database is empty returns an empty list
    @Test
    public void testGetAllEmployeesWhenEmpty() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // Edge Test Case: Verifies that adding an employee with invalid email format is handled correctly
    @Test
    public void testAddEmployeeWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"invalid-email\", \"department\": \"IT\"}"))
                .andExpect(status().isBadRequest());
    }

    // Edge Test Case: Verifies that adding an employee with too long first name is handled correctly
    @Test
    public void testAddEmployeeWithTooLongFirstName() throws Exception {
        String longFirstName = "a".repeat(256); // Assuming the limit is 255 characters
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"" + longFirstName + "\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"department\": \"IT\"}"))
                .andExpect(status().isBadRequest());
    }

    // Edge Test Case: Verifies that updating an employee with too long last name is handled correctly
    @Test
    public void testUpdateEmployeeWithTooLongLastName() throws Exception {
        String longLastName = "a".repeat(256); // Assuming the limit is 255 characters
        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"" + longLastName + "\", \"email\": \"john.doe@example.com\", \"department\": \"IT\"}"))
                .andExpect(status().isBadRequest());
    }

}