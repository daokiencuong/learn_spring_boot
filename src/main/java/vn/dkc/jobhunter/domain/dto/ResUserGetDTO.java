package vn.dkc.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.dkc.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResUserGetDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
}
