package vn.dkc.jobhunter.domain.response.Resume;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResResumeGetDTO {
    private long id;
    private String email;
    private String url;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
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
