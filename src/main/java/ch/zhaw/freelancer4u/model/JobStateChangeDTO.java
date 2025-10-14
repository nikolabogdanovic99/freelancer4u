package ch.zhaw.freelancer4u.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobStateChangeDTO {
    private String jobId;
    private String freelancerId;
}
