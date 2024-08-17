package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.ChangePasswordRequest;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ChangePasswordController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        Long employeeId = request.getEmployeeId();
        if (employeeId == null) {
            return ResponseEntity.badRequest().body("Employee ID must not be null!");
        }

        // Fetch the employee by ID
        Optional<Employee> employeeOpt = employeeService.getEmployeeById(employeeId);
        if (!employeeOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Employee not found");
        }

        Employee employee = employeeOpt.get();

        // Verify the old password
        if (!employeeService.checkOldPassword(request.getOldPassword(), employee)) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        // Change the password
        employeeService.changePassword(employeeId, request.getNewPassword());

        return ResponseEntity.ok("Password changed successfully");
    }
}
