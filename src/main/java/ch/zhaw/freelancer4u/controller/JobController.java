package ch.zhaw.freelancer4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobCreateDTO;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.CompanyService;
import ch.zhaw.freelancer4u.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @PostMapping("/job")
    public ResponseEntity<Job> createJob(@RequestBody JobCreateDTO cDTO) {

        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!companyService.companyExists(cDTO.getCompanyId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Job jDAO = new Job(
                cDTO.getDescription(),
                JobType.valueOf(cDTO.getJobType()),
                cDTO.getEarnings(),
                cDTO.getCompanyId());

        Job j = jobRepository.save(jDAO);
        return new ResponseEntity<>(j, HttpStatus.CREATED);
    }

    @GetMapping("/job")
    public ResponseEntity<List<Job>> getAllJobs(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) String type) {
        List<Job> jobs;
        if (min != null && type != null) {
            jobs = jobRepository.findByEarningsGreaterThanAndJobType(min, type);
        } else if (min != null) {
            jobs = jobRepository.findByEarningsGreaterThan(min);
        } else if (type != null) {
            jobs = jobRepository.findByJobType(type);
        } else {
            jobs = jobRepository.findAll();
        }
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable String id) {
        Optional<Job> result = jobRepository.findById(id);
        return result.map(job -> new ResponseEntity<>(job, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
