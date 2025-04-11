package vn.dkc.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.dto.ResultPaginationDTO;
import vn.dkc.jobhunter.domain.dto.UserCreateDTO;
import vn.dkc.jobhunter.domain.dto.UserGetDTO;
import vn.dkc.jobhunter.domain.dto.UserUpdateDTO;
import vn.dkc.jobhunter.service.UserService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

/**
 * Controller xử lý các yêu cầu liên quan đến quản lý người dùng Cung cấp các API endpoint CRUD
 * (Create, Read, Update, Delete) và phân trang cho đối tượng User
 */
@RestController
@RequestMapping("/api/${dkc.application.version}")
public class UserController {

    /**
     * Service xử lý logic nghiệp vụ liên quan đến User
     */
    private final UserService userService;

    /**
     * Encoder để mã hóa mật khẩu người dùng
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor để inject các dependency
     * 
     * @param userService service xử lý logic nghiệp vụ User
     * @param passwordEncoder service mã hóa mật khẩu
     */
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * API endpoint tạo mới một người dùng
     * 
     * @param postManUser thông tin người dùng cần tạo
     * @return ResponseEntity chứa thông tin người dùng đã được tạo
     */
    @PostMapping("/users")
    @ApiMessage("Create new user")
    public ResponseEntity<UserCreateDTO> createNewUser(@Valid @RequestBody User postManUser) {
        // Mã hóa mật khẩu trước khi lưu vào database
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        UserCreateDTO newUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    /**
     * API endpoint lấy thông tin một người dùng theo ID
     * 
     * @param id mã định danh của người dùng
     * @return ResponseEntity chứa thông tin người dùng
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetUserById(id));
    }

    /**
     * API endpoint lấy danh sách người dùng có phân trang
     */
    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getUserPaginate(
            @Filter Specification<User> spec,
            Pageable pageable) {

        return ResponseEntity.ok(this.userService.handleGetAllUser(spec, pageable));
    }

    /**
     * API endpoint cập nhật thông tin người dùng
     * 
     * @param user thông tin người dùng cần cập nhật
     * @return ResponseEntity chứa thông tin người dùng sau khi cập nhật
     */
    @PutMapping("/users")
    public ResponseEntity<UserUpdateDTO> updateUser(@RequestBody UserUpdateDTO user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(user));
    }


    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
