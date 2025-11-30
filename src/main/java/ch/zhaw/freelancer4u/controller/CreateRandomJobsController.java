package ch.zhaw.freelancer4u.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.CompanyRepository;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.UserService;

@RestController
@RequestMapping("/api")
public class CreateRandomJobsController {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserService userService;

    public CreateRandomJobsController(JobRepository jobRepository,
                                      CompanyRepository companyRepository,
                                      UserService userService) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.userService = userService;
    }

    @PostMapping("/randomjobs")
    public ResponseEntity<List<Job>> createRandomJobs(
            @RequestParam(name = "count", defaultValue = "3") int count) {

        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (count < 1) count = 1;
        if (count > 20) count = 20;

        var random = ThreadLocalRandom.current();

        // mindestens eine Firma sicherstellen
        List<Company> companies = companyRepository.findAll();
        if (companies.isEmpty()) {
            Company c = new Company("Random Company AG", "info@random-company.test");
            companies = List.of(companyRepository.save(c));
        }

        List<Job> jobsToSave = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Company c = companies.get(random.nextInt(companies.size()));

            String title = "Random Job " + (i + 1);
            String description = "Automatisch generierter Test-Job " + (i + 1);

            JobType[] types = JobType.values();
            JobType type = types[random.nextInt(types.length)];

            double earnings = 50 + random.nextInt(100); // 50–149

            Job job = new Job(title, description, type, earnings, c.getId());
            jobsToSave.add(job);
        }

        List<Job> saved = jobRepository.saveAll(jobsToSave);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
