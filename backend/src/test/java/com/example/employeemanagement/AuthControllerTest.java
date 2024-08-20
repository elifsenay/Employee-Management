package com.example.employeemanagement;

import com.example.employeemanagement.controller.AuthController;
import com.example.employeemanagement.model.AuthenticationRequest;
import com.example.employeemanagement.model.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test: Valid credentials should generate a token
    @Test
    public void testCreateAuthenticationToken_ValidCredentials() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmployeeId("valid-user");
        request.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        String generatedToken = "generated-jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername("valid-user")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(generatedToken);

        ResponseEntity<?> response = authController.createAuthenticationToken(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        AuthenticationResponse authResponse = (AuthenticationResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals(generatedToken, authResponse.getJwt());
    }

    // Negative Test: Invalid credentials should throw exception
    @Test
    public void testCreateAuthenticationToken_InvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmployeeId("invalid-user");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        Exception exception = assertThrows(Exception.class, () -> authController.createAuthenticationToken(request));
        assertEquals("Incorrect ID or password", exception.getMessage());
    }

}
