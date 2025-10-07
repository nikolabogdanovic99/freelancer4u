package ch.zhaw.freelancer4u.model;

import lombok.Getter;

@Getter
public class JobCreateDTO {
    private String title;
    private String description;
    private String jobType;
    private double earnings;
    private String companyId;
}
