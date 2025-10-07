package ch.zhaw.freelancer4u.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.freelancer4u.model.Job;

public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByEarningsGreaterThan(Double earnings);
    List<Job> findByJobType(String jobType);
    List<Job> findByEarningsGreaterThanAndJobType(Double earnings, String jobType);
}
