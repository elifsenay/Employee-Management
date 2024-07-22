package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.LeaveRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test Case: Save leave request successfully
    @Test
    public void testSaveLeaveRequest() {
        Employee employee = new Employee();
        employee.setRemainingLeaveDays(10);
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(2));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);

        LeaveRequest savedLeaveRequest = leaveRequestService.saveLeaveRequest(leaveRequest);

        assertNotNull(savedLeaveRequest);
        verify(employeeRepository, times(1)).save(employee);
        verify(leaveRequestRepository, times(1)).save(leaveRequest);
    }

    // Negative Test Case: Save leave request with insufficient leave days
    @Test
    public void testSaveLeaveRequestInsufficientLeaveDays() {
        Employee employee = new Employee();
        employee.setRemainingLeaveDays(1);
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(2));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(IllegalStateException.class, () -> leaveRequestService.saveLeaveRequest(leaveRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    // Negative Test Case: Save leave request for non-existent employee
    @Test
    public void testSaveLeaveRequestEmployeeNotFound() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(2));

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> leaveRequestService.saveLeaveRequest(leaveRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

    // Positive Test Case: Get leave request by ID
    @Test
    public void testGetLeaveRequestById() {
        LeaveRequest leaveRequest = new LeaveRequest();
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        Optional<LeaveRequest> foundLeaveRequest = leaveRequestService.getLeaveRequestById(1L);

        assertTrue(foundLeaveRequest.isPresent());
        assertEquals(leaveRequest, foundLeaveRequest.get());
    }

    // Positive Test Case: Get all leave requests
    @Test
    public void testGetAllLeaveRequests() {
        LeaveRequest leaveRequest1 = new LeaveRequest();
        LeaveRequest leaveRequest2 = new LeaveRequest();
        List<LeaveRequest> leaveRequests = Arrays.asList(leaveRequest1, leaveRequest2);
        when(leaveRequestRepository.findAll()).thenReturn(leaveRequests);

        List<LeaveRequest> allLeaveRequests = leaveRequestService.getAllLeaveRequests();

        assertEquals(2, allLeaveRequests.size());
        assertTrue(allLeaveRequests.contains(leaveRequest1));
        assertTrue(allLeaveRequests.contains(leaveRequest2));
    }

    /*
    // Positive Test Case: Delete leave request by ID
    @Test
    public void testDeleteLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest();
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        boolean isDeleted = leaveRequestService.deleteLeaveRequest(1L);

        assertTrue(isDeleted);
        verify(leaveRequestRepository, times(1)).delete(leaveRequest);
    }

    // Negative Test Case: Delete leave request with non-existent ID
    @Test
    public void testDeleteLeaveRequestWithNonExistentId() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.empty());

        boolean isDeleted = leaveRequestService.deleteLeaveRequest(1L);

        assertFalse(isDeleted);
    }

     */
}
