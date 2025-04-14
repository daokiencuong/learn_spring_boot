package vn.dkc.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.dkc.jobhunter.util.SecurityUtil;

import java.time.Instant;
import java.util.List;

/**
 * Entity đại diện cho thông tin của một công ty trong hệ thống Sử dụng JPA để mapping với bảng
 * 'companies' trong database
 * 
 * @Entity đánh dấu đây là một entity JPA
 * @Table chỉ định tên bảng trong database
 * @Getter @Setter tự động tạo các phương thức getter/setter (Lombok)
 */
@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {

    /**
     * ID của công ty, tự động tăng
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Tên công ty, không được để trống
     */
    @NotBlank(message = "Company's name must not blank...")
    private String name;

    /**
     * Mô tả về công ty Sử dụng MEDIUMTEXT để lưu trữ nội dung dài
     */
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    /**
     * Địa chỉ của công ty
     */
    private String address;

    /**
     * URL hoặc đường dẫn đến logo của công ty
     */
    private String logo;

    /**
     * Thời điểm tạo bản ghi, định dạng theo múi giờ GMT+7
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    /**
     * Thời điểm cập nhật bản ghi gần nhất
     */
    private Instant updatedAt;

    /**
     * Người tạo bản ghi
     */
    private String createdBy;

    /**
     * Người cập nhật bản ghi gần nhất
     */
    private String updatedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    List<User> users;

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
