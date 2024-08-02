package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    @NotNull
    Optional<Employee> findById(@NotNull Long id);

    boolean existsByEmail(String email);
}
