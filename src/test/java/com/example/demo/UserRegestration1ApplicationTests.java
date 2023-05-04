package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRegistrationSuccess() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("john.doe");
        request.setPassword("Abc123#$.");
        request.setIpAddress("70.49.51.14");

        ResponseEntity<UserRegistrationResponse> responseEntity = restTemplate.postForEntity("/register", request, UserRegistrationResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        UserRegistrationResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getUuid());
        assertTrue(response.getMessage().contains("john.doe"));
        assertTrue(response.getMessage().contains("Toronto"));
    }

    @Test
    public void testMissingFields() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("");
        request.setPassword("");
        request.setIpAddress("");

        ResponseEntity<UserRegistrationResponse> responseEntity = restTemplate.postForEntity("/register", request, UserRegistrationResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        UserRegistrationResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("All fields are required", response.getMessage());
    }

    @Test
    public void testInvalidPassword() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("jane.doe");
        request.setPassword("password");
        request.setIpAddress("70.49.51.14");

        ResponseEntity<UserRegistrationResponse> responseEntity = restTemplate.postForEntity("/register", request, UserRegistrationResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        UserRegistrationResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("Password must be at least 8 characters long and contain at least 1 number, 1 capitalized letter, and 1 special character in this set '_#$%.'", response.getMessage());
    }

    @Test
    public void testNonCanadianIP() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("jane.doe");
        request.setPassword("Abc123#$.");
        request.setIpAddress("8.8.8.8");

        ResponseEntity<UserRegistrationResponse> responseEntity = restTemplate.postForEntity("/register", request, UserRegistrationResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        UserRegistrationResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("User is not eligible to register", response.getMessage());
    }
}

