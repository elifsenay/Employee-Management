package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.LeaveRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@Transactional
public class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    @Captor
    private ArgumentCaptor<LeaveRequest> leaveRequestCaptor;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

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
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(2));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);

        LeaveRequest savedLeaveRequest = leaveRequestService.saveLeaveRequest(leaveRequest);

        assertNotNull(savedLeaveRequest);
        verify(employeeRepository, times(1)).save(employee);
        verify(leaveRequestRepository, times(1)).save(leaveRequest);
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

    // Positive Test Case: Updating an existing leave request successfully
    @Test
    public void testUpdateLeaveRequest() {
        LeaveRequest existingLeaveRequest = new LeaveRequest();
        existingLeaveRequest.setId(1L);
        existingLeaveRequest.setEmployee(existingLeaveRequest);
        existingLeaveRequest.setStartDate(LocalDate.of(2023, 7, 1));
        existingLeaveRequest.setEndDate(LocalDate.of(2023, 7, 10));
        existingLeaveRequest.setLeaveDays(10);

        LeaveRequest updatedLeaveRequest = new LeaveRequest();
        updatedLeaveRequest.setEmployeeId(1L);
        updatedLeaveRequest.setStartDate(LocalDate.of(2023, 7, 5));
        updatedLeaveRequest.setEndDate(LocalDate.of(2023, 7, 15));
        updatedLeaveRequest.setLeaveDays(11);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setRemainingLeaveDays(20);

        when(leaveRequestRepository.findById(anyLong())).thenReturn(Optional.of(existingLeaveRequest));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(updatedLeaveRequest);

        Optional<LeaveRequest> result = leaveRequestService.updateLeaveRequest(1L, updatedLeaveRequest);
        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).save(employeeCaptor.capture());
        verify(leaveRequestRepository, times(1)).save(leaveRequestCaptor.capture());

        Employee savedEmployee = employeeCaptor.getValue();
        LeaveRequest savedLeaveRequest = leaveRequestCaptor.getValue();

        assertEquals(19, savedEmployee.getRemainingLeaveDays());
        assertEquals(LocalDate.of(2023, 7, 5), savedLeaveRequest.getStartDate());
        assertEquals(LocalDate.of(2023, 7, 15), savedLeaveRequest.getEndDate());
        assertEquals(11, savedLeaveRequest.getLeaveDays());
    }

    // Positive Test Case: Deleting an existing leave request successfully
    @Test
    public void testDeleteLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(1L);
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setLeaveDays(10);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setRemainingLeaveDays(10);

        when(leaveRequestRepository.findById(anyLong())).thenReturn(Optional.of(leaveRequest));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        boolean result = leaveRequestService.deleteLeaveRequest(1L);
        assertTrue(result);
        verify(employeeRepository, times(1)).save(employeeCaptor.capture());
        verify(leaveRequestRepository, times(1)).delete(leaveRequestCaptor.capture());

        Employee savedEmployee = employeeCaptor.getValue();
        LeaveRequest deletedLeaveRequest = leaveRequestCaptor.getValue();

        assertEquals(20, savedEmployee.getRemainingLeaveDays());  // Correct calculation
        assertEquals(1L, deletedLeaveRequest.getId().longValue());
    }

    // Positive Test Case: Save leave request with maximum allowed leave days
    @Test
    public void testSaveLeaveRequestWithMaxLeaveDays() {
        Employee employee = new Employee();
        employee.setRemainingLeaveDays(30); // Assuming 30 allowed leave days
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(15));

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

    // Negative Test Case: Updating an existing leave request with invalid data (negative leave days)
    @Test(expected = IllegalStateException.class)
    public void testUpdateLeaveRequestWithInvalidData() {
        LeaveRequest existingLeaveRequest = new LeaveRequest();
        existingLeaveRequest.setId(1L);
        existingLeaveRequest.setEmployeeId(1L);
        existingLeaveRequest.setStartDate(LocalDate.of(2023, 7, 1));
        existingLeaveRequest.setEndDate(LocalDate.of(2023, 7, 10));
        existingLeaveRequest.setLeaveDays(10);

        LeaveRequest invalidLeaveRequest = new LeaveRequest();
        invalidLeaveRequest.setEmployeeId(1L);
        invalidLeaveRequest.setStartDate(LocalDate.of(2023, 7, 10));
        invalidLeaveRequest.setEndDate(LocalDate.of(2023, 7, 5));
        invalidLeaveRequest.setLeaveDays(-5);

        when(leaveRequestRepository.findById(anyLong())).thenReturn(Optional.of(existingLeaveRequest));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(new Employee()));

        leaveRequestService.updateLeaveRequest(1L, invalidLeaveRequest);
    }

    // Negative Test Case: Updating a non-existent leave request
    @Test
    public void testUpdateNonExistingLeaveRequest() {
        when(leaveRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        LeaveRequest updatedLeaveRequest = new LeaveRequest();
        updatedLeaveRequest.setEmployeeId(1L);
        updatedLeaveRequest.setStartDate(LocalDate.of(2023, 7, 5));
        updatedLeaveRequest.setEndDate(LocalDate.of(2023, 7, 15));
        updatedLeaveRequest.setLeaveDays(11);

        Optional<LeaveRequest> result = leaveRequestService.updateLeaveRequest(1L, updatedLeaveRequest);
        assertFalse(result.isPresent());
    }

    // Negative Test Case: Deleting a non-existent leave request
    @Test
    public void testDeleteLeaveRequest_Negative() {
        when(leaveRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = leaveRequestService.deleteLeaveRequest(1L);
        assertFalse(result);
    }

    // Negative Test Case: Save leave request where the end date is before the start date
    @Test
    public void testSaveLeaveRequestWithEndDateBeforeStartDate() {
        Employee employee = new Employee();
        employee.setRemainingLeaveDays(10);
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(LocalDate.now().plusDays(5));
        leaveRequest.setEndDate(LocalDate.now().plusDays(2));  // End date is before start date

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(IllegalStateException.class, () -> leaveRequestService.saveLeaveRequest(leaveRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }


    // Edge Test Case: Save leave request with null dates
    @Test
    public void testSaveLeaveRequestWithNullDates() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(null);
        leaveRequest.setEndDate(null);

        assertThrows(IllegalArgumentException.class, () -> leaveRequestService.saveLeaveRequest(leaveRequest));

        verify(employeeRepository, never()).save(any(Employee.class));
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }


    // Edge Test Case: Boundary condition for leave days (zero days)
    @Test
    public void testSaveLeaveRequestWithZeroLeaveDays() {
        Employee employee = new Employee();
        employee.setRemainingLeaveDays(10);
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now()); // Zero days leave

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);

        LeaveRequest savedLeaveRequest = leaveRequestService.saveLeaveRequest(leaveRequest);

        assertNotNull(savedLeaveRequest);
        verify(employeeRepository, times(1)).save(employee);
        verify(leaveRequestRepository, times(1)).save(leaveRequest);
    }

    // Edge Test Case: Delete leave request with null ID
    @Test
    public void testDeleteLeaveRequestWithNullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaveRequestService.deleteLeaveRequest(null));
        assertEquals("Leave request ID cannot be null", exception.getMessage());
        verify(leaveRequestRepository, never()).findById(any());
        verify(leaveRequestRepository, never()).delete(any(LeaveRequest.class));
    }

    // Edge Test Case: Save leave request with overlapping dates
    @Test
    public void testSaveLeaveRequestWithOverlappingDates() {
        Employee employee = new Employee();
        employee.setRemainingLeaveDays(10);

        LeaveRequest existingRequest = new LeaveRequest();
        existingRequest.setEmployeeId(1L);
        existingRequest.setStartDate(LocalDate.now().plusDays(1));
        existingRequest.setEndDate(LocalDate.now().plusDays(3));

        LeaveRequest newRequest = new LeaveRequest();
        newRequest.setEmployeeId(1L);
        newRequest.setStartDate(LocalDate.now().plusDays(2));
        newRequest.setEndDate(LocalDate.now().plusDays(4));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(leaveRequestRepository.findByEmployeeId(1L)).thenReturn(Arrays.asList(existingRequest));

        assertThrows(IllegalArgumentException.class, () -> leaveRequestService.saveLeaveRequest(newRequest));
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }

}
