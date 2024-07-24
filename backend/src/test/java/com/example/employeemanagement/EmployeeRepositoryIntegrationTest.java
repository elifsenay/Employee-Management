package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class EmployeeRepositoryIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setRemainingLeaveDays(15);
        employeeRepository.save(employee);
    }

    @Test
    public void testFindById() {
        Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testSaveEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Jane");
        newEmployee.setLastName("Doe");
        newEmployee.setEmail("jane.doe@example.com");
        newEmployee.setDepartment("HR");
        newEmployee.setRemainingLeaveDays(20);

        Employee savedEmployee = employeeRepository.save(newEmployee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();
    }

    @Test
    public void testDeleteEmployee() {
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());
        assertThat(foundEmployee).isNotPresent();
    }

    @Test
    public void testUpdateEmployee() {
        employee.setDepartment("Finance");
        employeeRepository.save(employee);
        Optional<Employee> updatedEmployee = employeeRepository.findById(employee.getId());
        assertThat(updatedEmployee).isPresent();
        assertThat(updatedEmployee.get().getDepartment()).isEqualTo("Finance");
    }
}