package ch.zhaw.freelancer4u.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Document("job")
public class Job {
    @Id
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private String jobType;

    @NonNull
    private Double earnings;   

    @NonNull
    private String companyId;

    private String jobState = "NEW";
    private String freelancerId;
}
