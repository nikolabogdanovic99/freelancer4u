package ch.zhaw.freelancer4u.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobStateAggregationDTO;
import ch.zhaw.freelancer4u.model.JobStateChangeDTO;
import ch.zhaw.freelancer4u.model.Mail;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.JobService;
import ch.zhaw.freelancer4u.service.MailService;
import ch.zhaw.freelancer4u.service.UserService;

@RestController
@RequestMapping("/api/service")
public class JobServiceController {
    @Autowired
    JobService jobService;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @PutMapping("/assignjob")
    public ResponseEntity<Job> assignJob(@RequestBody JobStateChangeDTO changeS) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String freelancerId = changeS.getFreelancerId();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.assignJob(jobId, freelancerId);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/completejob")
    public ResponseEntity<Job> completeJob(@RequestBody JobStateChangeDTO changeS) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String freelancerId = changeS.getFreelancerId();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.completeJob(jobId, freelancerId);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/assignjob")
    public ResponseEntity<Job> assignToMe(@RequestParam String jobId) {
        String userEmail = userService.getEmail();
        Optional<Job> job = jobService.assignJob(jobId, userEmail);
        if (job.isPresent()) {
            // Woche 12: E-Mail senden bei Job-Zuweisung
            Job assignedJob = job.get();
            Mail mail = new Mail();
            mail.setTo(userEmail);
            mail.setSubject("Job Assigned: " + assignedJob.getDescription());
            mail.setMessage("Hello,\n\n" +
                    "The job \"" + assignedJob.getDescription() + "\" has been assigned to you.\n\n" +
                    "Job Details:\n" +
                    "- Description: " + assignedJob.getDescription() + "\n" +
                    "- Type: " + assignedJob.getJobType() + "\n" +
                    "- Earnings: " + assignedJob.getEarnings() + " CHF\n" +
                    "- Status: " + assignedJob.getJobState() + "\n\n" +
                    "Best regards,\n" +
                    "Freelancer4U Team");
            mailService.sendMail(mail);
            
            return new ResponseEntity<>(assignedJob, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/completejob")
    public ResponseEntity<Job> completeMyJob(@RequestParam String jobId) {
        String userEmail = userService.getEmail();
        Optional<Job> job = jobService.completeJob(jobId, userEmail);
        if (job.isPresent()) {
            // Woche 12: E-Mail senden bei Job-Abschluss
            Job completedJob = job.get();
            Mail mail = new Mail();
            mail.setTo(userEmail);
            mail.setSubject("Job Completed: " + completedJob.getDescription());
            mail.setMessage("Hello,\n\n" +
                    "Congratulations! The job \"" + completedJob.getDescription() + "\" has been completed.\n\n" +
                    "Job Details:\n" +
                    "- Description: " + completedJob.getDescription() + "\n" +
                    "- Type: " + completedJob.getJobType() + "\n" +
                    "- Earnings: " + completedJob.getEarnings() + " CHF\n" +
                    "- Status: " + completedJob.getJobState() + "\n\n" +
                    "Thank you for your work!\n\n" +
                    "Best regards,\n" +
                    "Freelancer4U Team");
            mailService.sendMail(mail);
            
            return new ResponseEntity<>(completedJob, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/jobdashboard")
    public List<JobStateAggregationDTO> getJobStateAggregation(@RequestParam String company) {
        return jobRepository.getJobStateAggregation(company);
    }
}