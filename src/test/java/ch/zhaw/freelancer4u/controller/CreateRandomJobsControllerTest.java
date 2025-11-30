package ch.zhaw.freelancer4u.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.repository.CompanyRepository;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.security.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class CreateRandomJobsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void testCreateRandomJobsCreatesJobs() throws Exception {
        // sicherstellen, dass es mindestens eine Firma gibt
        if (companyRepository.findAll().isEmpty()) {
            companyRepository.save(new Company("Test Company", "info@testcompany.ch"));
        }

        long before = jobRepository.count();

        mvc.perform(post("/api/randomjobs")
                        .param("count", "3")
                        .header(HttpHeaders.AUTHORIZATION, TestSecurityConfig.ADMIN))
                .andExpect(status().isCreated());

        long after = jobRepository.count();

        assertTrue(after >= before + 1, "Es sollten neue Jobs erstellt worden sein");
    }

    @Test
    public void testCreateRandomJobsForbiddenForNonAdmin() throws Exception {
        mvc.perform(post("/api/randomjobs")
                        .param("count", "2")
                        .header(HttpHeaders.AUTHORIZATION, TestSecurityConfig.USER))
                .andExpect(status().isForbidden());
    }
}
