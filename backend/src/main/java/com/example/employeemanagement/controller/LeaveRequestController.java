package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaverequests")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    public LeaveRequest addLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        return leaveRequestService.saveLeaveRequest(leaveRequest);
    }

    @GetMapping
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestService.getAllLeaveRequests();
    }
}
