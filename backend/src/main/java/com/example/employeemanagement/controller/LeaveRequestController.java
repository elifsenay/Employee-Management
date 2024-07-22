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


   /* @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        logger.info("Received request to delete leave request with ID: " + id);
        boolean isDeleted = leaveRequestService.deleteLeaveRequest(id);
        if (!isDeleted) {
            logger.warn("Leave request with ID " + id + " not found");
            throw new ResourceNotFoundException("LeaveRequest not found with id " + id);
        }
        logger.info("Leave request with ID " + id + " deleted successfully");
        return ResponseEntity.noContent().build();
    }
    */
}
