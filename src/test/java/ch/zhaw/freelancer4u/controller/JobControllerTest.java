package ch.zhaw.freelancer4u.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.zhaw.freelancer4u.repository.CompanyRepository;
import ch.zhaw.freelancer4u.security.TestSecurityConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@TestMethodOrder(OrderAnnotation.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CompanyRepository companyRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String createdJobId;
    private static String usedCompanyId;

    private String getCompanyId() {
        return companyRepository.findAll().get(0).getId();
    }

    @Test
    @Order(1)
    void createJob() throws Exception {
        usedCompanyId = getCompanyId();

        String jsonBody = String.format(
            "{\"title\":\"Test Job\",\"description\":\"Desc\",\"jobType\":\"IMPLEMENT\",\"earnings\":123.0,\"companyId\":\"%s\"}",
            usedCompanyId
        );

        MvcResult result = mvc.perform(post("/api/job")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, TestSecurityConfig.ADMIN)
            .content(jsonBody))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(jsonResponse);
        createdJobId = node.get("id").asText();
        assertNotNull(createdJobId);
    }

    @Test
    @Order(2)
    void getJobById_shouldReturnCreatedJob() throws Exception {
        mvc.perform(get("/api/job/" + createdJobId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, TestSecurityConfig.ADMIN))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Test Job"))
            .andExpect(jsonPath("$.description").value("Desc"))
            .andExpect(jsonPath("$.companyId").value(usedCompanyId));
    }

    @Test
    @Order(3)
    void deleteJobById_shouldReturnOk() throws Exception {
        mvc.perform(delete("/api/job/" + createdJobId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, TestSecurityConfig.ADMIN))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void getJobById_afterDeletion_shouldReturnNotFound() throws Exception {
        mvc.perform(get("/api/job/" + createdJobId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, TestSecurityConfig.ADMIN))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}
