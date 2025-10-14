package ch.zhaw.freelancer4u.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobStateAggregationDTO;

public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByEarningsGreaterThan(Double earnings);

    List<Job> findByJobType(String jobType);

    List<Job> findByEarningsGreaterThanAndJobType(Double earnings, String jobType);

    @Aggregation({
            "{ '$match': { 'companyId': ?0 } }",
            "{ '$group': { '_id': '$jobState', 'jobIds': { '$push': '$_id' }, 'count': { '$sum': 1 } } }"
    })
    List<JobStateAggregationDTO> getJobStateAggregation(String companyId);

}
