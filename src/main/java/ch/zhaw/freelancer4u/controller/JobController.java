package ch.zhaw.freelancer4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobCreateDTO;
import ch.zhaw.freelancer4u.repository.JobRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @PostMapping("/job")
    public ResponseEntity<Job> createJob(@RequestBody JobCreateDTO dto) {
        try {
            Job job = new Job(
                    dto.getTitle(),
                    dto.getDescription(),
                    dto.getJobType(),
                    dto.getEarnings(),
                    dto.getCompanyId());
            Job saved = jobRepository.save(job);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/job")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return ResponseEntity.ok(jobs);
    }

@GetMapping("/job/{id}")
public ResponseEntity<Job> getJobById(@PathVariable String id) {
    Optional<Job> result = jobRepository.findById(id);
    if (result.isPresent()) {
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
}
