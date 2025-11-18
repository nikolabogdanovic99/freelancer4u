package ch.zhaw.freelancer4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobStateChangeDTO;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.JobService;
import ch.zhaw.freelancer4u.service.UserService;
import ch.zhaw.freelancer4u.model.JobStateAggregationDTO;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/service")
public class JobServiceController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    UserService userService;

    @PutMapping("/assignjob")
    public ResponseEntity<Job> assignJob(@RequestBody JobStateChangeDTO change) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String freelancerId = change.getFreelancerId();
        String jobId = change.getJobId();

        Optional<Job> job = jobService.assignJob(jobId, freelancerId);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/completejob")
    public ResponseEntity<Job> completeJob(@RequestBody JobStateChangeDTO change) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String freelancerId = change.getFreelancerId();
        String jobId = change.getJobId();

        Optional<Job> job = jobService.completeJob(jobId, freelancerId);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/jobdashboard")
    public List<JobStateAggregationDTO> getJobStateAggregation(@RequestParam String company) {
        return jobRepository.getJobStateAggregation(company);
    }

    @PutMapping("/me/assignjob")
    public ResponseEntity<Job> assignToMe(@RequestParam String jobId) {
        String userEmail = userService.getEmail();
        Optional<Job> job = jobService.assignJob(jobId, userEmail);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

 @PutMapping("/me/completejob")
public ResponseEntity<Job> completeJobForMe(@RequestParam String jobId) {
    String email = userService.getEmail();

    Optional<Job> job = jobService.completeJob(jobId, email);

    if (job.isPresent()) {
        return new ResponseEntity<>(job.get(), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
}

}
