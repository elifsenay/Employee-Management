package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LeaveRequestService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestService.class);

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
        // Check for null dates
        if (leaveRequest.getStartDate() == null || leaveRequest.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(leaveRequest.getEmployeeId());

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            long daysBetween = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;

            // Check for invalid date range
            if (daysBetween < 0) {
                throw new IllegalStateException("End date cannot be before start date");
            }

            // Check for overlapping dates
            List<LeaveRequest> existingRequests = leaveRequestRepository.findByEmployeeId(leaveRequest.getEmployeeId());
            for (LeaveRequest existingRequest : existingRequests) {
                if (!(leaveRequest.getEndDate().isBefore(existingRequest.getStartDate()) ||
                        leaveRequest.getStartDate().isAfter(existingRequest.getEndDate()))) {
                    throw new IllegalArgumentException("Leave request dates overlap with an existing request");
                }
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
        if (id == null) {
            throw new IllegalArgumentException("Leave request ID cannot be null");
        }

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

    public String saveDocument(MultipartFile file, Long leaveRequestId) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new IllegalArgumentException("Invalid file type. Only images and PDFs are allowed.");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5 MB limit
            throw new IllegalArgumentException("File size exceeds the limit of 5MB.");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        Files.createDirectories(filePath.getParent());

        Files.copy(file.getInputStream(), filePath);

        System.out.println("Document saved at path" + filePath);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        leaveRequest.setDocumentPath(filePath.toString());
        leaveRequestRepository.save(leaveRequest);

        return filePath.toString();
    }


}
