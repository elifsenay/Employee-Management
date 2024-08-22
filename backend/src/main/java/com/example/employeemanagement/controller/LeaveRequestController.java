package com.example.employeemanagement.controller;

import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @PostMapping("/{leaveRequestId}/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @PathVariable("leaveRequestId") Long leaveRequestId) {
        try {
            String documentPath = leaveRequestService.saveDocument(file, leaveRequestId);
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

    @GetMapping("/{id}/document")
    public ResponseEntity<Resource> viewDocument(@RequestParam("path") String documentPath) throws IOException {
        Path filePath = Paths.get(documentPath);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File not found or not readable");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);
    }

}
