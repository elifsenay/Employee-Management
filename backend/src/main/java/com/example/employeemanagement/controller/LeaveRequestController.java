package com.example.employeemanagement.controller;

import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.service.EmailService;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/leaverequests")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<LeaveRequest> addLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequest savedLeaveRequest = leaveRequestService.saveLeaveRequest(leaveRequest);

        Long employeeId = leaveRequest.getEmployeeId();
        String employeeEmail = employeeService.getEmailById(employeeId);
        emailService.sendEmail(employeeEmail, "Leave Request Created", "Your leave request has been successfully submitted.");

        return new ResponseEntity<>(savedLeaveRequest, HttpStatus.OK);
    }

    @PostMapping("/{leaveRequestId}/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @PathVariable("leaveRequestId") Long leaveRequestId) {
        try {
            String documentPath = leaveRequestService.saveDocument(file, leaveRequestId);

           LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(leaveRequestId)
                    .orElseThrow(() -> new ResourceNotFoundException("Leave Request not found with id " + leaveRequestId));
            Long employeeId = leaveRequest.getEmployeeId();
            String employeeEmail = employeeService.getEmailById(employeeId);
            emailService.sendEmail(employeeEmail, "Document Uploaded", "A new document has been uploaded to your leave request.");

            return ResponseEntity.ok("Document uploaded successfully. Path: " + documentPath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload document");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

        String email = employeeService.getEmailById(updatedLeaveRequest.getEmployeeId());
        emailService.sendEmail(email, "Leave Request Updated.", "Your leave request has been updated successfully.");

        return new ResponseEntity<>(updatedLeaveRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest not found with id " + id));

        boolean isDeleted = leaveRequestService.deleteLeaveRequest(id);
        if (!isDeleted) {
            throw new ResourceNotFoundException("LeaveRequest not found with id " + id);
        }

        String email = employeeService.getEmailById(leaveRequest.getEmployeeId());
        emailService.sendEmail(email, "Leave Request Deleted", "Your leave request has been deleted successfully.");

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{leaveRequestId}/document")
    public ResponseEntity<InputStreamResource> getDocument(@PathVariable Long leaveRequestId) throws IOException {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Request not found with id " + leaveRequestId));

        String documentPath = leaveRequest.getDocumentPath();

        if (documentPath == null || documentPath.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(documentPath);
        if (Files.exists(path)) {
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{leaveRequestId}/document")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long leaveRequestId) throws IOException {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest not found with id " + leaveRequestId));

        leaveRequestService.deleteDocument(leaveRequestId);

        String email = employeeService.getEmailById(leaveRequest.getEmployeeId());
        emailService.sendEmail(email, "Document Deleted", "The document associated with your leave request has been deleted successfully.");

        return ResponseEntity.noContent().build();
    }

}
