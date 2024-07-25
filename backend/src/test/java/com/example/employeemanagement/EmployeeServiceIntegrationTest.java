package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll(); // Clear the database before each test
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setRemainingLeaveDays(15);
        employeeRepository.save(employee);
    }

    @Test
    public void testSaveEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Jane");
        newEmployee.setLastName("Doe");
        newEmployee.setEmail("jane.doe@example.com");
        newEmployee.setDepartment("HR");
        newEmployee.setRemainingLeaveDays(20);

        Employee savedEmployee = employeeService.saveEmployee(newEmployee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();
    }

    @Test
    public void testFindEmployeeById() {
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(employee.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testUpdateEmployee() {
        employee.setDepartment("Finance");
        employeeService.saveEmployee(employee);

        Optional<Employee> updatedEmployee = employeeService.getEmployeeById(employee.getId());
        assertThat(updatedEmployee).isPresent();
        assertThat(updatedEmployee.get().getDepartment()).isEqualTo("Finance");
    }

    @Test
    public void testDeleteEmployee() {
        boolean isDeleted = employeeService.deleteEmployee(employee.getId());
        assertThat(isDeleted).isTrue();
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(employee.getId());
        assertThat(foundEmployee).isEmpty();
    }

    @Test
    public void testGetAllEmployees() {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Jane");
        newEmployee.setLastName("Doe");
        newEmployee.setEmail("jane.doe@example.com");
        newEmployee.setDepartment("HR");
        newEmployee.setRemainingLeaveDays(20);
        employeeRepository.save(newEmployee);

        assertThat(employeeService.getAllEmployees().size()).isEqualTo(2);
    }
}
