package com.example.employeemanagement.service;

import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id).orElse(null);
    }

    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequestDetails) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);
        if (leaveRequest != null) {
            leaveRequest.setEmployeeId(leaveRequestDetails.getEmployeeId());
            leaveRequest.setLeaveDays(leaveRequestDetails.getLeaveDays());
            return leaveRequestRepository.save(leaveRequest);
        }
        return null;
    }

    public boolean deleteLeaveRequest(Long id) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);
        if (leaveRequest != null) {
            leaveRequestRepository.delete(leaveRequest);
            return true;
        }
        return false;
    }
}
