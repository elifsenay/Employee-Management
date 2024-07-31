package com.example.employeemanagement.controller;

import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/leaverequests")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveRequestController {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestController.class);

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequest> addLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequest savedLeaveRequest = leaveRequestService.saveLeaveRequest(leaveRequest);
        return new ResponseEntity<>(savedLeaveRequest, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest not found with id " + id));
        return new ResponseEntity<>(leaveRequest, HttpStatus.OK);
    }

    @GetMapping
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestService.getAllLeaveRequests();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(@PathVariable Long id, @RequestBody LeaveRequest leaveRequestDetails) {
        LeaveRequest updatedLeaveRequest = leaveRequestService.updateLeaveRequest(id, leaveRequestDetails)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest not found with id " + id));
        return new ResponseEntity<>(updatedLeaveRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        boolean isDeleted = leaveRequestService.deleteLeaveRequest(id);
        if (!isDeleted) {
            throw new ResourceNotFoundException("LeaveRequest not found with id " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
