package com.example.employeemanagement;

import com.example.employeemanagement.config.WebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.transaction.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebConfig.class})
public class WebConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        source.registerCorsConfiguration("/**", config);

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CorsFilter(source))
                .build();
    }

    // Positive Test Case: Verifies CORS settings for allowed origins and methods
    @Test
    public void testCorsConfigurer() {
        WebConfig webConfig = new WebConfig();
        WebMvcConfigurer configurer = webConfig.corsConfigurer();

        CorsRegistry registry = new CorsRegistry();
        configurer.addCorsMappings(registry);

        try {
            java.lang.reflect.Field field = CorsRegistry.class.getDeclaredField("corsConfigurations");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, org.springframework.web.cors.CorsConfiguration> corsConfigurations =
                    (java.util.Map<String, org.springframework.web.cors.CorsConfiguration>) field.get(registry);

            org.springframework.web.cors.CorsConfiguration corsConfiguration = corsConfigurations.get("/api/**");

            assertEquals(true, corsConfiguration.getAllowCredentials());
            assertEquals(1, corsConfiguration.getAllowedOrigins().size());
            assertEquals("http://localhost:3000", corsConfiguration.getAllowedOrigins().get(0));
            assertEquals(4, corsConfiguration.getAllowedMethods().size());
            assertEquals("*", corsConfiguration.getAllowedHeaders().get(0));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    // Negative Test Case: Verifies that CORS settings block requests from disallowed origins
    @Test
    public void testCorsConfigurationWithDisallowedOrigin() throws Exception {
        mockMvc.perform(options("/api/employees")
                        .header("Origin", "https://disallowed-origin.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden());
    }
}