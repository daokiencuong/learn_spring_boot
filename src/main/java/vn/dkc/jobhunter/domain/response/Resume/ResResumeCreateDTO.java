package vn.dkc.jobhunter.domain.response.Resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumeCreateDTO {
    private long id;
    private Instant createdAt;
    private String createdBy;
}
