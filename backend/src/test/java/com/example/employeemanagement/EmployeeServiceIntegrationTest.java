package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll(); // Clear the database before each test
        Employee existingEmployee = new Employee();
        existingEmployee.setFirstName("Existing");
        existingEmployee.setLastName("Employee");
        existingEmployee.setEmail("existing@example.com");
        existingEmployee.setDepartment("HR");
        employeeRepository.save(existingEmployee);
    }

    @Test
    public void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        Employee savedEmployee = employeeService.saveEmployee(employee);

        Employee updatedDetails = new Employee();
        updatedDetails.setFirstName("Jane");
        updatedDetails.setLastName("Smith");
        updatedDetails.setEmail("existing@example.com"); // Intentional conflict email
        updatedDetails.setDepartment("IT");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.updateEmployee(savedEmployee.getId(), updatedDetails));

        String expectedMessage = "Email already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
