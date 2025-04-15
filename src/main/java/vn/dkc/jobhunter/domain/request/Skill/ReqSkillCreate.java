package vn.dkc.jobhunter.domain.request.Skill;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqSkillCreate {
    @NotBlank(message = "Name is required")
    private String name;
}
