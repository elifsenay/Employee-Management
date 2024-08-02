package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Employee loginDetails) {
        Employee employee = employeeService.getEmployeeById(loginDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (employee.getPassword().equals(loginDetails.getPassword())) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
