package com.example.employeemanagement.controller;

import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        try {
            Long employeeId = Long.parseLong(id);
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isPresent()) {
                return ResponseEntity.ok(employee.get());
            } else {
                throw new ResourceNotFoundException("Employee not found with id " + id);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request for invalid ID
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employeeDetails) {
        Optional<Employee> updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        return updatedEmployee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        try {
            Long employeeId = Long.parseLong(id);
            if (employeeService.deleteEmployee(employeeId)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build(); // Return 400 Bad Request for invalid ID
        }
    }
}
