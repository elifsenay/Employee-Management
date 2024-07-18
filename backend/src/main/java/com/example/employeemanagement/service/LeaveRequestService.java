package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(leaveRequest.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            long daysBetween = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate());
            if (employee.getRemainingLeaveDays() >= daysBetween) {
                employee.setRemainingLeaveDays(employee.getRemainingLeaveDays() - (int) daysBetween);
                employeeRepository.save(employee);
                return leaveRequestRepository.save(leaveRequest);
            } else {
                throw new IllegalStateException("Not enough leave days available");
            }
        } else {
            throw new IllegalArgumentException("Employee not found");
        }
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }
}
