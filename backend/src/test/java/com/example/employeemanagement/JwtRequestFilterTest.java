package com.example.employeemanagement;

import com.example.employeemanagement.config.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    // Positive Test Case: Valid JWT token and valid user should set authentication in SecurityContext
    @Test
    public void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
        String validToken = "valid-token";
        String username = "valid-user";

        when(jwtUtil.extractUsername(validToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(jwtUtil.validateToken(validToken, userDetails)).thenReturn(true);

        request.addHeader("Authorization", "Bearer " + validToken);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "Authentication token should not be null");
        assertEquals(username, authentication.getName(), "Authentication name should match username");

        verify(filterChain, times(1)).doFilter(request, response);
    }

    // Negative Test Case: Missing Authorization header should not set authentication
    @Test
    public void testDoFilterInternal_MissingAuthorizationHeader() throws ServletException, IOException {
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    // Negative Test Case: Invalid JWT token should not set authentication
    @Test
    public void testDoFilterInternal_InvalidJwt() throws ServletException, IOException {
        String invalidToken = "invalid-token";
        String username = "invalid-user";

        request.addHeader("Authorization", "Bearer " + invalidToken);
        when(jwtUtil.extractUsername(invalidToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(invalidToken, userDetails)).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    // Edge Test Case: Valid Authorization header but no Bearer prefix -> should not set authentication
    @Test
    public void testDoFilterInternal_InvalidAuthorizationHeader() throws ServletException, IOException {
        request.addHeader("Authorization", "SomeOtherToken");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
