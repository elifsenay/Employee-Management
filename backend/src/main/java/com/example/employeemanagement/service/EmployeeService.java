package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Employee saveEmployee(Employee employee) {
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> updateEmployee(Long id, Employee employeeDetails) {
        return employeeRepository.findById(id).map(employee -> {
            // Check if the new email already exists for another employee
            if (employeeDetails.getEmail() != null &&
                    (employee.getEmail() == null || !employee.getEmail().equals(employeeDetails.getEmail())) &&
                    employeeRepository.existsByEmail(employeeDetails.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            // Update the employee details
            if (employeeDetails.getFirstName() != null) {
                employee.setFirstName(employeeDetails.getFirstName());
            }
            if (employeeDetails.getLastName() != null) {
                employee.setLastName(employeeDetails.getLastName());
            }
            if (employeeDetails.getEmail() != null) {
                employee.setEmail(employeeDetails.getEmail());
            }
            if (employeeDetails.getDepartment() != null) {
                employee.setDepartment(employeeDetails.getDepartment());
            }
            if (employeeDetails.getRemainingLeaveDays() != 0) {
                employee.setRemainingLeaveDays(employeeDetails.getRemainingLeaveDays());
            }

            return employeeRepository.save(employee);
        });
    }


    public boolean deleteEmployee(Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employeeRepository.delete(employee);
            return true;
        }).orElse(false);
    }
}
