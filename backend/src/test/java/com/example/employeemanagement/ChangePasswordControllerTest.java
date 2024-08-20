package com.example.employeemanagement;

import com.example.employeemanagement.controller.ChangePasswordController;
import com.example.employeemanagement.model.ChangePasswordRequest;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest
public class ChangePasswordControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private ChangePasswordController changePasswordController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangePassword_Success() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmployeeId(1L);
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");

        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));
        when(employeeService.checkOldPassword("oldPassword", employee)).thenReturn(true);

        ResponseEntity<?> response = changePasswordController.changePassword(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password changed successfully", response.getBody());
    }

    @Test
    public void testChangePassword_InvalidEmployee() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmployeeId(1L);

        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = changePasswordController.changePassword(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Employee not found", response.getBody());
    }

    @Test
    public void testChangePassword_IncorrectOldPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmployeeId(1L);
        request.setOldPassword("wrongPassword");

        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));
        when(employeeService.checkOldPassword("wrongPassword", employee)).thenReturn(false);

        ResponseEntity<?> response = changePasswordController.changePassword(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Old password is incorrect", response.getBody());
    }

    @Test
    public void testChangePassword_NullEmployeeId() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmployeeId(null);

        ResponseEntity<?> response = changePasswordController.changePassword(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Employee ID must not be null!", response.getBody());
    }
}
