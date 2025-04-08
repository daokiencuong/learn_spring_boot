package vn.dkc.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.dkc.jobhunter.util.SecurityUtil;

import java.time.Instant;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Company's name must not blank...")
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;

    private String logo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @PrePersist
    public void handleCreateAt(){
       this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
               SecurityUtil.getCurrentUserLogin().get()
               :
               ""
       ;
       this.createdAt = Instant.now();
    }
    @PreUpdate
    public void handleUpdateAt(){
        this.updatedAt = Instant.now();

        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
                SecurityUtil.getCurrentUserLogin().get()
                :
                ""
        ;
    }
}
