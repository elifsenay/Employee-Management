package com.example.employeemanagement;

import com.example.employeemanagement.controller.LeaveRequestController;
import com.example.employeemanagement.exception.GlobalExceptionHandler;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import com.example.employeemanagement.service.LeaveRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class LeaveRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LeaveRequestService leaveRequestService;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

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
    public void testGetAllLeaveRequests_Positive() throws Exception {
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

    // Negative Test Case: Retrieving a non-existent leave request by ID
    @Test
    public void testGetLeaveRequestById_Negative() throws Exception {
        when(leaveRequestService.getLeaveRequestById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/leaverequests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}