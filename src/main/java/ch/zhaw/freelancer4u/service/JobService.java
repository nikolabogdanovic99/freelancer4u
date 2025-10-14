package ch.zhaw.freelancer4u.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.repository.JobRepository;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    public Optional<Job> assignJob(String jobId, String freelancerId) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isEmpty()) {
            return Optional.empty();
        }

        Job job = optionalJob.get();

        if (!"NEW".equals(job.getJobState())) {
            return Optional.empty();
        }

        job.setJobState("ASSIGNED");
        job.setFreelancerId(freelancerId);

        Job savedJob = jobRepository.save(job);

        return Optional.of(savedJob);
    }

    public Optional<Job> completeJob(String jobId, String freelancerId) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isEmpty()) {
            return Optional.empty();
        }

        Job job = optionalJob.get();

        if (!"ASSIGNED".equals(job.getJobState())) {
            return Optional.empty();
        }

        if (!freelancerId.equals(job.getFreelancerId())) {
            return Optional.empty();
        }

        job.setJobState("DONE");

        Job saved = jobRepository.save(job);

        return Optional.of(saved);
    }
}