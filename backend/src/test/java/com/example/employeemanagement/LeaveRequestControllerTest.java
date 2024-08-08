package com.example.employeemanagement;

import com.example.employeemanagement.controller.LeaveRequestController;
import com.example.employeemanagement.exception.GlobalExceptionHandler;
import com.example.employeemanagement.model.Employee;
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
import org.springframework.http.MediaType;
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

    @Test
    public void testAddLeaveRequest() throws Exception {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(1L);
        leaveRequest.setEmployee(new Employee());
        leaveRequest.setStartDate(LocalDate.of(2023, 7, 1));
        leaveRequest.setEndDate(LocalDate.of(2023, 7, 10));

        when(leaveRequestService.saveLeaveRequest(any(LeaveRequest.class))).thenReturn(leaveRequest);

        mockMvc.perform(post("/api/leaverequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\": 1, \"startDate\": \"2023-07-01\", \"endDate\": \"2023-07-10\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1));
    }

    @Test
    public void testGetLeaveRequestById() throws Exception {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(1L);
        leaveRequest.setEmployee(new Employee());

        when(leaveRequestService.getLeaveRequestById(anyLong())).thenReturn(Optional.of(leaveRequest));

        mockMvc.perform(get("/api/leaverequests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1));
    }

    @Test
    public void testGetAllLeaveRequests() throws Exception {
        LeaveRequest leaveRequest1 = new LeaveRequest();
        leaveRequest1.setId(1L);
        leaveRequest1.setEmployee(new Employee());
        leaveRequest1.setStartDate(LocalDate.of(2023, 7, 1));
        leaveRequest1.setEndDate(LocalDate.of(2023, 7, 10));

        LeaveRequest leaveRequest2 = new LeaveRequest();
        leaveRequest2.setId(2L);
        leaveRequest2.setEmployee(new Employee());
        leaveRequest2.setStartDate(LocalDate.of(2023, 8, 1));
        leaveRequest2.setEndDate(LocalDate.of(2023, 8, 10));

        when(leaveRequestService.getAllLeaveRequests()).thenReturn(Arrays.asList(leaveRequest1, leaveRequest2));

        mockMvc.perform(get("/api/leaverequests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(1))
                .andExpect(jsonPath("$[1].employeeId").value(2));
    }

    @Test
    public void testGetLeaveRequestById_Negative() throws Exception {
        when(leaveRequestService.getLeaveRequestById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/leaverequests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
