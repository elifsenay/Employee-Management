package com.example.employeemanagement;

import com.example.employeemanagement.model.LeaveRequest;
import com.example.employeemanagement.repository.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class LeaveRequestRepositoryIntegrationTest {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    private LeaveRequest leaveRequest;

    @BeforeEach
    public void setUp() {
        leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(1L);
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
        newLeaveRequest.setEmployeeId(1L);
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
