package com.example.employeemanagement;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EmployeeControllerTest.class,
        EmployeeServiceTest.class,
        EmployeeControllerIntegrationTest.class,
        EmployeeServiceTest.class,
        LeaveRequestControllerTest.class,
        LeaveRequestServiceTest.class,
        LeaveRequestServiceTest.class,
        GlobalExceptionHandlerTest.class,
        SecurityConfigTest.class,
        WebConfigTest.class
})
public class TestSuite {
}
