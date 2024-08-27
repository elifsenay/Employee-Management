package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll(); // Clear the database before each test
        Employee existingEmployee = new Employee("Existing", "Employee", "existing@example.com", "HR", 15, "password","ADMIN");
        employeeRepository.save(existingEmployee);
    }

    @Test
    public void testUpdateEmployee() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 15, "password","ADMIN");
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
