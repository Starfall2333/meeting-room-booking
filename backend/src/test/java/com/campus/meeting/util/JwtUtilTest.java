package com.campus.meeting.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateAndValidateToken() {
        String username = "testuser";
        String role = "USER";
        String token = jwtUtil.generateToken(username, role);
        assertNotNull(token);

        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);

        String extractedRole = jwtUtil.extractRole(token);
        assertEquals(role, extractedRole);

        assertTrue(jwtUtil.isTokenValid(token));
        assertTrue(jwtUtil.validateToken(token, username));
    }
}
