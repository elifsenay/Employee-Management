package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/leaverequests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<LeaveRequest> addLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        Optional<Employee> employeeOpt = employeeRepository.findById(leaveRequest.getEmployee().getId());

        if (!employeeOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Employee employee = employeeOpt.get();

        // Calculate leave days and ensure employee has enough remaining leave days
        long daysBetween = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;

        if (daysBetween < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (employee.getRemainingLeaveDays() >= daysBetween) {
            employee.setRemainingLeaveDays(employee.getRemainingLeaveDays() - (int) daysBetween);
            employeeRepository.save(employee);
            leaveRequest.setLeaveDays((int) daysBetween);
            LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
            return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(id);
        return leaveRequestOpt.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(id);

        if (!leaveRequestOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LeaveRequest leaveRequest = leaveRequestOpt.get();
        Employee employee = leaveRequest.getEmployee();

        if (employee != null) {
            employee.setRemainingLeaveDays(employee.getRemainingLeaveDays() + leaveRequest.getLeaveDays());
            employeeRepository.save(employee);
        }

        leaveRequestRepository.delete(leaveRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(
            @PathVariable Long id,
            @RequestBody LeaveRequest updatedLeaveRequest) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(id);

        if (!leaveRequestOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LeaveRequest existingLeaveRequest = leaveRequestOpt.get();
        Employee employee = existingLeaveRequest.getEmployee();

        // Ensure employee ID matches
        if (!employee.getId().equals(updatedLeaveRequest.getEmployee().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Calculate new leave days
        long daysBetween = ChronoUnit.DAYS.between(updatedLeaveRequest.getStartDate(), updatedLeaveRequest.getEndDate()) + 1;

        if (daysBetween < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Update remaining leave days
        int newRemainingLeaveDays = employee.getRemainingLeaveDays()
                + existingLeaveRequest.getLeaveDays()
                - (int) daysBetween;

        if (newRemainingLeaveDays >= 0) {
            employee.setRemainingLeaveDays(newRemainingLeaveDays);
            employeeRepository.save(employee);

            // Update leave request fields
            existingLeaveRequest.setStartDate(updatedLeaveRequest.getStartDate());
            existingLeaveRequest.setEndDate(updatedLeaveRequest.getEndDate());
            existingLeaveRequest.setLeaveDays((int) daysBetween);

            leaveRequestRepository.save(existingLeaveRequest);
            return new ResponseEntity<>(existingLeaveRequest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
