package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class LeaveRequestRepositoryIntegrationTest {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private LeaveRequest leaveRequest;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        // Create and save an Employee
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("Engineering");
        employee.setRemainingLeaveDays(20);
        employeeRepository.save(employee);

        // Create and save a LeaveRequest
        leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);  // Set the Employee object
        leaveRequest.setStartDate(LocalDate.of(2023, 7, 1));
        leaveRequest.setEndDate(LocalDate.of(2023, 7, 10));
        leaveRequest.setLeaveDays(10);
        leaveRequestRepository.save(leaveRequest);
    }

    @Test
    public void testFindById() {
        Optional<LeaveRequest> foundLeaveRequest = leaveRequestRepository.findById(leaveRequest.getId());
        assertThat(foundLeaveRequest).isPresent();
        assertThat(foundLeaveRequest.get().getLeaveDays()).isEqualTo(10);
    }

    @Test
    public void testSaveLeaveRequest() {
        LeaveRequest newLeaveRequest = new LeaveRequest();
        newLeaveRequest.setEmployee(employee);  // Set the Employee object
        newLeaveRequest.setStartDate(LocalDate.of(2023, 7, 15));
        newLeaveRequest.setEndDate(LocalDate.of(2023, 7, 20));
        newLeaveRequest.setLeaveDays(6);

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(newLeaveRequest);
        assertThat(savedLeaveRequest).isNotNull();
        assertThat(savedLeaveRequest.getId()).isNotNull();
    }

    @Test
    public void testDeleteLeaveRequest() {
        leaveRequestRepository.deleteById(leaveRequest.getId());
        Optional<LeaveRequest> foundLeaveRequest = leaveRequestRepository.findById(leaveRequest.getId());
        assertThat(foundLeaveRequest).isNotPresent();
    }

    @Test
    public void testUpdateLeaveRequest() {
        leaveRequest.setLeaveDays(15);
        leaveRequestRepository.save(leaveRequest);
        Optional<LeaveRequest> updatedLeaveRequest = leaveRequestRepository.findById(leaveRequest.getId());
        assertThat(updatedLeaveRequest).isPresent();
        assertThat(updatedLeaveRequest.get().getLeaveDays()).isEqualTo(15);
    }
}
