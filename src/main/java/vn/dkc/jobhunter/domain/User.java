package vn.dkc.jobhunter.domain;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
    private String email;

    /**
     * Mật khẩu của người dùng (đã được mã hóa)
     */
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
