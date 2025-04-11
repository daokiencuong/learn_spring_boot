package vn.dkc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.dto.LoginDTO;
import vn.dkc.jobhunter.domain.dto.ResLoginDTO;
import vn.dkc.jobhunter.service.UserService;
import vn.dkc.jobhunter.util.SecurityUtil;

/**
 * Controller xử lý các yêu cầu liên quan đến xác thực người dùng Cung cấp API endpoint cho việc
 * đăng nhập và tạo JWT token
 */
@RestController
@RequestMapping("/api/${dkc.application.version}")
public class AuthController {
    /**
     * Dependency Injection các service cần thiết authenticationManagerBuilder: Xử lý xác thực người
     * dùng securityUtil: Tiện ích tạo và quản lý JWT token
     */
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    /**
     * API endpoint xử lý đăng nhập người dùng
     * 
     * @param loginDTO đối tượng chứa thông tin đăng nhập (username, password)
     * @return ResponseEntity chứa JWT token nếu đăng nhập thành công
     */
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // Tạo đối tượng xác thực từ username và password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                        loginDTO.getPassword());

        // Thực hiện xác thực người dùng
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Tạo JWT token từ thông tin xác thực
        String access_token = this.securityUtil.createToken(authentication);
        // Lưu thông tin xác thực vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo response chứa token
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        User currentUserDB = this.userService.handleGetUserByEmail(loginDTO.getUsername());

        if(currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = resLoginDTO.new UserLogin(
                            currentUserDB.getId(),
                            currentUserDB.getEmail(),
                            currentUserDB.getName()
            );

            resLoginDTO.setUser(userLogin);
        }

        resLoginDTO.setAccessToken(access_token);

        return ResponseEntity.status(HttpStatus.OK).body(resLoginDTO);
    }
}
