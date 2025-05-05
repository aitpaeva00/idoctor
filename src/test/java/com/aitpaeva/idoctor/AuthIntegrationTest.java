package com.aitpaeva.idoctor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullAuthFlow() throws Exception {
        String username = "test";
        String email = "test@example.com";
        String password = "1234";

        // Step 1: Try to register user
        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}", username, email, password)))
                .andReturn();

        int registerStatus = registerResult.getResponse().getStatus();
        String registerBody = registerResult.getResponse().getContentAsString();

        // Step 2: If user already exists (403), delete and retry
        if (registerStatus == 403 && registerBody.contains("Username already exists")) {
            mockMvc.perform(post("/api/auth/delete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format("{\"username\":\"test\", \"password\":\"1234\"}", username, password)))
                    .andExpect(status().isOk());

            // Retry registration
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format("{\"username\":\"test\", \"email\":\"test@sample.com\", \"password\":\"1234\"}", username, email, password)))
                    .andExpect(status().isOk());
        }

        // Step 3: Login and parse tokens
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"username\":\"test\", \"password\":\"1234\"}", username, password)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        String accessToken = jsonNode.get("accessToken").asText();
        String refreshToken = jsonNode.get("refreshToken").asText();

        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);
    }
}
