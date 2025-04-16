package vn.dkc.jobhunter.domain.response.Resume;

import lombok.Getter;
import lombok.Setter;
import vn.dkc.jobhunter.util.constant.StatusResumeEnum;

import java.time.Instant;

@Getter
@Setter
public class ResResumeGetDTO {
    private long id;
    private String email;
    private String url;
    private StatusResumeEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String companyName;
    private UserResume user;
    private JobResume job;

    @Getter
    @Setter
    public static class UserResume {
        private long id;
        private String name;
    }

    @Setter
    @Getter
    public static class JobResume {
        private long id;
        private String name;
    }

}
