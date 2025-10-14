package ch.zhaw.freelancer4u.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobStateAggregationDTO {
    private String id;           
    private List<String> jobIds; 
    private int count;
}
