package com.example.employeemanagement;

import com.example.employeemanagement.controller.LeaveRequestController;
import com.example.employeemanagement.exception.GlobalExceptionHandler;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.service.LeaveRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@Transactional
public class LeaveRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LeaveRequestService leaveRequestService;

    @InjectMocks
    private LeaveRequestController leaveRequestController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(leaveRequestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // Positive Test Case: Adding a new leave request successfully
    @Test
    public void testAddLeaveRequest() throws Exception {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(1L);
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(LocalDate.of(2023, 7, 1));
        leaveRequest.setEndDate(LocalDate.of(2023, 7, 10));

        when(leaveRequestService.saveLeaveRequest(any(LeaveRequest.class))).thenReturn(leaveRequest);

        mockMvc.perform(post("/api/leaverequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\": 1, \"startDate\": \"2023-07-01\", \"endDate\": \"2023-07-10\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1));
    }

    // Positive Test Case: Retrieving an existing leave request by ID successfully
    @Test
    public void testGetLeaveRequestById() throws Exception {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(1L);
        leaveRequest.setEmployeeId(1L);

        when(leaveRequestService.getLeaveRequestById(anyLong())).thenReturn(Optional.of(leaveRequest));

        mockMvc.perform(get("/api/leaverequests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1));
    }

    // Positive Test Case: Retrieving all leave requests successfully
    @Test
    public void testGetAllLeaveRequests() throws Exception {
        LeaveRequest leaveRequest1 = new LeaveRequest();
        leaveRequest1.setId(1L);
        leaveRequest1.setEmployeeId(1L);
        leaveRequest1.setStartDate(LocalDate.of(2023, 7, 1));
        leaveRequest1.setEndDate(LocalDate.of(2023, 7, 10));

        LeaveRequest leaveRequest2 = new LeaveRequest();
        leaveRequest2.setId(2L);
        leaveRequest2.setEmployeeId(2L);
        leaveRequest2.setStartDate(LocalDate.of(2023, 8, 1));
        leaveRequest2.setEndDate(LocalDate.of(2023, 8, 10));

        when(leaveRequestService.getAllLeaveRequests()).thenReturn(Arrays.asList(leaveRequest1, leaveRequest2));

        mockMvc.perform(get("/api/leaverequests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(1))
                .andExpect(jsonPath("$[1].employeeId").value(2));
    }

    // Positive Test Case: Successfully uploading a valid document
    @Test
    public void testUploadDocumentSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Sample PDF Content".getBytes()
        );

        when(leaveRequestService.saveDocument(any(), anyLong())).thenReturn("/uploads/test.pdf");

        mockMvc.perform(multipart("/api/leaverequests/1/upload")
                        .file(file)
                        .param("leaveRequestId", "1")
                        .header("Authorization", "Bearer test_token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document uploaded successfully. Path: /uploads/test.pdf"));
    }

    // Negative Test Case: Uploading a file with an invalid file type
    @Test
    public void testUploadDocumentInvalidFileType() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.exe", MediaType.APPLICATION_OCTET_STREAM_VALUE, "Executable content".getBytes()
        );

        when(leaveRequestService.saveDocument(any(), anyLong())).thenThrow(new IllegalArgumentException("Invalid file type. Only images and PDFs are allowed."));

        mockMvc.perform(multipart("/api/leaverequests/1/upload")
                        .file(file)
                        .param("leaveRequestId", "1")
                        .header("Authorization", "Bearer test_token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid file type. Only images and PDFs are allowed."));
    }

    // Negative Test Case: Uploading a file that exceeds the size limit
    @Test
    public void testUploadDocumentSizeLimitExceeded() throws Exception {
        byte[] largeFileContent = new byte[6 * 1024 * 1024]; // 6MB
        MockMultipartFile file = new MockMultipartFile(
                "file", "large.pdf", MediaType.APPLICATION_PDF_VALUE, largeFileContent
        );

        when(leaveRequestService.saveDocument(any(), anyLong())).thenThrow(new IllegalArgumentException("File size exceeds the limit of 5MB."));

        mockMvc.perform(multipart("/api/leaverequests/1/upload")
                        .file(file)
                        .param("leaveRequestId", "1")
                        .header("Authorization", "Bearer test_token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("File size exceeds the limit of 5MB."));
    }


    // Negative Test Case: Retrieving a non-existent leave request by ID
    @Test
    public void testGetLeaveRequestById_Negative() throws Exception {
        when(leaveRequestService.getLeaveRequestById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/leaverequests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}