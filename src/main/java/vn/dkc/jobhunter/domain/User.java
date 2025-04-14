package vn.dkc.jobhunter.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.DynamicInsert;

import vn.dkc.jobhunter.util.SecurityUtil;
import vn.dkc.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

/**
 * Entity đại diện cho người dùng trong hệ thống Sử dụng JPA để mapping với bảng 'users' trong
 * database
 * 
 * @Entity đánh dấu đây là một entity JPA
 * @Table chỉ định tên bảng trong database
 * @DynamicUpdate chỉ cập nhật các trường đã thay đổi
 * @DynamicInsert chỉ insert các trường có giá trị
 */
@Entity
@Table(name = "users")
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
public class User {
    /**
     * ID của người dùng, tự động tăng
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Tên người dùng
     */
    private String name;

    /**
     * Email của người dùng, dùng để đăng nhập
     */
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * Mật khẩu của người dùng (đã được mã hóa)
     */
    @NotBlank(message = "Password is required")
    private String password;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    /**
     * Xử lý tự động trước khi lưu bản ghi mới Cập nhật thời gian tạo và người tạo
     */
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
