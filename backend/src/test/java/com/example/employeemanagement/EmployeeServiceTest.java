package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test Case: Save employee successfully
    @Test
    public void testSaveEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals(employee, savedEmployee);
        verify(employeeRepository, times(1)).save(employee);
    }

    // Positive Test Case: Get all employees
    @Test
    public void testGetAllEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> allEmployees = employeeService.getAllEmployees();

        assertEquals(2, allEmployees.size());
        assertTrue(allEmployees.contains(employee1));
        assertTrue(allEmployees.contains(employee2));
    }

    // Positive Test Case: Get employee by ID
    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertTrue(foundEmployee.isPresent());
        assertEquals(employee, foundEmployee.get());
    }

    // Negative Test Case: Get employee by non-existent ID
    @Test
    public void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertFalse(foundEmployee.isPresent());
    }

    // Positive Test Case: Update employee successfully
    @Test
    public void testUpdateEmployee() {
        Employee existingEmployee = new Employee();
        Employee updatedDetails = new Employee();
        updatedDetails.setFirstName("John");
        updatedDetails.setLastName("Doe");
        updatedDetails.setEmail("john.doe@example.com");
        updatedDetails.setDepartment("IT");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        Optional<Employee> updatedEmployee = employeeService.updateEmployee(1L, updatedDetails);

        assertTrue(updatedEmployee.isPresent());
        assertEquals(existingEmployee, updatedEmployee.get());
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    // Negative Test Case: Update employee with non-existent ID
    @Test
    public void testUpdateEmployeeNotFound() {
        Employee updatedDetails = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Employee> updatedEmployee = employeeService.updateEmployee(1L, updatedDetails);

        assertFalse(updatedEmployee.isPresent());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // Positive Test Case: Delete employee successfully
    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        boolean isDeleted = employeeService.deleteEmployee(1L);

        assertTrue(isDeleted);
        verify(employeeRepository, times(1)).delete(employee);
    }

    // Negative Test Case: Delete employee with non-existent ID
    @Test
    public void testDeleteEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        boolean isDeleted = employeeService.deleteEmployee(1L);

        assertFalse(isDeleted);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
}
