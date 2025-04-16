package vn.dkc.jobhunter.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.dkc.jobhunter.util.SecurityUtil;
import vn.dkc.jobhunter.util.constant.StatusResumeEnum;

import java.time.Instant;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "URL không được để trống")
    private String url;

    @Enumerated(EnumType.STRING)
    private StatusResumeEnum status;

    private Instant createdAt;
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @PrePersist
    public void handleCreateAt() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    /**
     * Xử lý tự động trước khi cập nhật bản ghi Cập nhật thời gian sửa và người sửa
     */
    @PreUpdate
    public void handleUpdateAt() {
        this.updatedAt = Instant.now();

        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
    }
}
