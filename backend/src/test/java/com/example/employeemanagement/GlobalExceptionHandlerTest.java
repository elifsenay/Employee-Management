package com.example.employeemanagement;

import com.example.employeemanagement.exception.GlobalExceptionHandler;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test Case: Verifies that IllegalArgumentException is handled correctly
    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Illegal argument", responseEntity.getBody());
    }

    // Positive Test Case: Verifies that IllegalStateException is handled correctly
    @Test
    public void testHandleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Illegal state");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Illegal state", responseEntity.getBody());
    }

    // Positive Test Case: Verifies that generic Exception is handled correctly
    @Test
    public void testHandleException() {
        Exception ex = new Exception("Internal server error");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An error occurred. Please try again.", responseEntity.getBody());
    }

    // Positive Test Case: Verifies that ResourceNotFoundException is handled correctly
    @Test
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Resource not found", responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when IllegalArgumentException message is null
    @Test
    public void testHandleIllegalArgumentExceptionWithNullMessage() {
        IllegalArgumentException ex = new IllegalArgumentException((String) null);
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when IllegalArgumentException message is empty
    @Test
    public void testHandleIllegalArgumentExceptionWithEmptyMessage() {
        IllegalArgumentException ex = new IllegalArgumentException("");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when IllegalStateException message is null
    @Test
    public void testHandleIllegalStateExceptionWithNullMessage() {
        IllegalStateException ex = new IllegalStateException((String) null);
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when IllegalStateException message is empty
    @Test
    public void testHandleIllegalStateExceptionWithEmptyMessage() {
        IllegalStateException ex = new IllegalStateException("");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when generic Exception message is null
    @Test
    public void testHandleExceptionWithNullMessage() {
        Exception ex = new Exception((String) null);
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An error occurred. Please try again.", responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when generic Exception message is empty
    @Test
    public void testHandleExceptionWithEmptyMessage() {
        Exception ex = new Exception("");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An error occurred. Please try again.", responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when ResourceNotFoundException message is null
    @Test
    public void testHandleResourceNotFoundExceptionWithNullMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException((String) null);
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    // Negative Test Case: Verifies handling when ResourceNotFoundException message is empty
    @Test
    public void testHandleResourceNotFoundExceptionWithEmptyMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("");
        when(webRequest.getDescription(false)).thenReturn("some details");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody());
    }
}
