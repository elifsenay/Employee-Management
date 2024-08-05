package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerIntegrationTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        employeeRepository.deleteAll(); // Clear the database before each test
    }

    // Positive Test Case: Add a new employee successfully
    @Test
    public void testAddEmployee() throws Exception {
        String employeeJson = "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"department\": \"IT\", \"password\": \"password\"}";

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.department", is("IT")))
                .andExpect(jsonPath("$.password", is("password")));
    }

    // Positive Test Case: Delete an existing employee successfully
    @Test
    public void testDeleteEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 15, "password");
        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(delete("/api/employees/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // Positive Test Case: Retrieve all employees successfully
    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee1 = new Employee("John", "Doe", "john.doe@example.com", "IT", 15, "password");
        Employee employee2 = new Employee("Jane", "Doe", "jane.doe@example.com", "HR", 15, "password");
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    // Positive Test Case: Retrieve an employee by ID successfully
    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 15, "password");
        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(get("/api/employees/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    // Positive Test Case: Update an existing employee successfully
    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 15, "password");
        Employee savedEmployee = employeeRepository.save(employee);

        String updatedEmployeeJson = "{\"firstName\": \"John\", \"lastName\": \"Smith\", \"email\": \"john.smith@example.com\", \"department\": \"IT\", \"password\": \"password\"}";

        mockMvc.perform(put("/api/employees/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.email", is("john.smith@example.com")));
    }

    // Negative Test Case: Update an employee with non-existent ID
    @Test
    public void testUpdateEmployeeNotFound() throws Exception {
        String updatedEmployeeJson = "{\"firstName\": \"John\", \"lastName\": \"Smith\", \"email\": \"john.smith@example.com\", \"department\": \"IT\", \"password\": \"password\"}";

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isNotFound());
    }

    // Negative Test Case: Delete an employee with non-existent ID
    @Test
    public void testDeleteEmployeeNotFound() throws Exception {
        mockMvc.perform(delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
