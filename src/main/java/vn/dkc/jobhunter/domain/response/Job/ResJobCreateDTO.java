package vn.dkc.jobhunter.domain.response.Job;

import lombok.Getter;
import lombok.Setter;
import vn.dkc.jobhunter.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResJobCreateDTO {
    private Long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private List<String> skills;
    private Instant createdAt;
    private String createdBy;
}
