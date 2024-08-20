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
import org.springframework.boot.test.context.SpringBootTest;
import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@Transactional
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // Negative Test Case: Get employee by non-existent ID
    @Test
    public void testGetEmployeeByIdNotFound() {
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertFalse(foundEmployee.isPresent());
    }



    // Negative Test Case: Update employee with non-existent ID
    @Test
    public void testUpdateEmployeeNotFound() {
        Employee updatedDetails = new Employee();

        Optional<Employee> updatedEmployee = employeeService.updateEmployee(1L, updatedDetails);

        assertFalse(updatedEmployee.isPresent());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // Negative Test Case: Delete employee with non-existent ID
    @Test
    public void testDeleteEmployeeNotFound() {
        boolean isDeleted = employeeService.deleteEmployee(1L);

        assertFalse(isDeleted);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
    @Test
    public void testExistsByEmail() {
        when(employeeRepository.existsByEmail("test@example.com")).thenReturn(true);
        boolean exists = employeeRepository.existsByEmail("test@example.com");
        assertTrue(exists);
        verify(employeeRepository, times(1)).existsByEmail("test@example.com");
    }


    /* Edge Test Case: Save employee with null fields
    @Test
    public void testSaveEmployeeWithNullFields() {
        Employee employee = new Employee();
        employee.setFirstName(null);
        employee.setLastName(null);
        employee.setEmail(null);
        employee.setDepartment(null);

        // Since the repository should handle null fields gracefully,
        // we should not throw an exception from the repository.
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee);
        assertNull(savedEmployee.getFirstName());
        assertNull(savedEmployee.getLastName());
        assertNull(savedEmployee.getEmail());
        assertNull(savedEmployee.getDepartment());
    }

    // Edge Test Case: Update employee with partial information
    @Test
    public void testUpdateEmployeeWithPartialInformation() {
        Employee existingEmployee = new Employee();
        existingEmployee.setFirstName("OldFirstName");
        existingEmployee.setLastName("OldLastName");
        existingEmployee.setEmail("old.email@example.com");
        existingEmployee.setDepartment("OldDepartment");
        existingEmployee.setRemainingLeaveDays(15);

        Employee updatedDetails = new Employee();
        updatedDetails.setFirstName("NewFirstName");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        Optional<Employee> updatedEmployee = employeeService.updateEmployee(1L, updatedDetails);

        assertTrue(updatedEmployee.isPresent());
        assertEquals("NewFirstName", updatedEmployee.get().getFirstName());
        assertEquals("OldLastName", updatedEmployee.get().getLastName());
        assertEquals("old.email@example.com", updatedEmployee.get().getEmail());
        assertEquals("OldDepartment", updatedEmployee.get().getDepartment());
        assertEquals(15, updatedEmployee.get().getRemainingLeaveDays());
    }

    // Edge Test Case: Save employee with existing email
    @Test
    public void testSaveEmployeeWithDuplicateEmail() {
        Employee employee = new Employee();
        employee.setEmail("duplicate@example.com");

        when(employeeRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // Test Case: Save employee successfully
    @Test
    public void testSaveEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPassword("password");

        String hashedPassword = "hashedPassword";
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn(hashedPassword);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals("John", savedEmployee.getFirstName());
        assertEquals("Doe", savedEmployee.getLastName());
        assertEquals("john.doe@example.com", savedEmployee.getEmail());
        assertEquals(hashedPassword, savedEmployee.getPassword());

        verify(employeeRepository, times(1)).save(employee);
    }

    // Test Case: Get employee by ID successfully
    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertTrue(foundEmployee.isPresent());
        assertEquals(employee, foundEmployee.get());
        verify(employeeRepository, times(1)).findById(1L);
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
 */


}
