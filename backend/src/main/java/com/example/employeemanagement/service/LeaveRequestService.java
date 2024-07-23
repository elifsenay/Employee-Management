package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestService.class);

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(leaveRequest.getEmployeeId());
            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();
                long daysBetween = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;

                // Check for invalid date range
                if (daysBetween < 0) {
                    throw new IllegalStateException("End date cannot be before start date");
                }
                leaveRequest.setLeaveDays((int) daysBetween);
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
        } catch (Exception e) {
            logger.error("Error saving leave request", e);
            throw e;
        }
    }


    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public Optional<LeaveRequest> getLeaveRequestById(Long id) {
        logger.info("Fetching leave request with ID: " + id);
        return leaveRequestRepository.findById(id);
    }

    public Optional<LeaveRequest> updateLeaveRequest(Long id, LeaveRequest leaveRequestDetails) {
        try {
            Optional<LeaveRequest> optionalLeaveRequest = leaveRequestRepository.findById(id);
            if (optionalLeaveRequest.isPresent()) {
                LeaveRequest existingLeaveRequest = optionalLeaveRequest.get();

                Optional<Employee> optionalEmployee = employeeRepository.findById(existingLeaveRequest.getEmployeeId());
                if (optionalEmployee.isPresent()) {
                    Employee employee = optionalEmployee.get();

                    // Rollback previous leave days
                    employee.setRemainingLeaveDays(employee.getRemainingLeaveDays() + existingLeaveRequest.getLeaveDays());

                    // Update leave request details
                    existingLeaveRequest.setStartDate(leaveRequestDetails.getStartDate());
                    existingLeaveRequest.setEndDate(leaveRequestDetails.getEndDate());
                    long daysBetween = ChronoUnit.DAYS.between(leaveRequestDetails.getStartDate(), leaveRequestDetails.getEndDate()) + 1;

                    // Check for invalid date range
                    if (daysBetween < 0) {
                        throw new IllegalStateException("End date cannot be before start date");
                    }
                    existingLeaveRequest.setLeaveDays((int) daysBetween);

                    if (employee.getRemainingLeaveDays() >= daysBetween) {
                        employee.setRemainingLeaveDays(employee.getRemainingLeaveDays() - (int) daysBetween);
                        employeeRepository.save(employee);
                        return Optional.of(leaveRequestRepository.save(existingLeaveRequest));
                    } else {
                        throw new IllegalStateException("Not enough leave days available");
                    }
                } else {
                    throw new IllegalArgumentException("Employee not found");
                }
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error updating leave request", e);
            throw e;
        }
    }


    public boolean deleteLeaveRequest(Long id) {
        try {
            return leaveRequestRepository.findById(id).map(leaveRequest -> {
                Optional<Employee> optionalEmployee = employeeRepository.findById(leaveRequest.getEmployeeId());
                if (optionalEmployee.isPresent()) {
                    Employee employee = optionalEmployee.get();
                    employee.setRemainingLeaveDays(employee.getRemainingLeaveDays() + leaveRequest.getLeaveDays());
                    employeeRepository.save(employee);
                }
                leaveRequestRepository.delete(leaveRequest);
                logger.info("Deleted leave request with ID: " + id);
                return true;
            }).orElseGet(() -> {
                logger.warn("Leave request with ID " + id + " not found");
                return false;
            });
        } catch (Exception e) {
            logger.error("Error deleting leave request", e);
            throw e;
        }
    }
}
