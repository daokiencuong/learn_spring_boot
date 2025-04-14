package vn.dkc.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.request.ReqLoginDTO;
import vn.dkc.jobhunter.domain.response.ResLoginDTO;
import vn.dkc.jobhunter.service.UserService;
import vn.dkc.jobhunter.util.SecurityUtil;
import vn.dkc.jobhunter.util.annotation.ApiMessage;
import vn.dkc.jobhunter.util.error.IdInvalidException;

/**
 * Controller xử lý các yêu cầu liên quan đến xác thực người dùng Cung cấp API endpoint cho việc
 * đăng nhập và tạo JWT token
 */
@RestController
@RequestMapping("/api/${dkc.application.version}/auth")
public class AuthController {

    @Value("${dkc.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

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
     * @param reqLoginDTO đối tượng chứa thông tin đăng nhập (username, password)
     * @return ResponseEntity chứa JWT token nếu đăng nhập thành công
     */
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        // Tạo đối tượng xác thực từ username và password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(),
                        reqLoginDTO.getPassword());

        // Thực hiện xác thực người dùng
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Lưu thông tin xác thực vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo response chứa token
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        User currentUserDB = this.userService.handleGetUserByEmail(reqLoginDTO.getUsername());

        if(currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                            currentUserDB.getId(),
                            currentUserDB.getEmail(),
                            currentUserDB.getName()
            );

            resLoginDTO.setUser(userLogin);
        }
        // Tạo JWT token từ thông tin xác thực
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());

        resLoginDTO.setAccessToken(access_token);

        // Tạo refresh token
        String refresh_token = this.securityUtil.createRefreshToken(reqLoginDTO.getUsername(), resLoginDTO);

        //update User
        this.userService.updateUserToken(reqLoginDTO.getUsername(), refresh_token);

        //Set Cookie
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLoginDTO);
    }

    @GetMapping("/account")
    @ApiMessage("Fetch Acount")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.handleGetUserByEmail(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if(currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
            userGetAccount.setUser(userLogin);
        }

        return ResponseEntity.ok(userGetAccount);
    }

    @GetMapping("refresh")
    @ApiMessage("Refresh Token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token") String refresh_token) throws IdInvalidException {

        // Kiểm tra xem refresh token có hợp lệ hay không
        Jwt decoded_Token = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decoded_Token.getSubject();

        //Check user by email
        User currentUserDB = this.userService.getUserByRefeshTokenAndEmail(refresh_token, email);

        if(currentUserDB == null){
            throw new IdInvalidException("Refresh token không hợp lệ");
        }
            // Tạo response chứa token
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        if(currentUserDB != null) {
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                        currentUserDB.getId(),
                        currentUserDB.getEmail(),
                        currentUserDB.getName()
                );

                resLoginDTO.setUser(userLogin);
            }
            // Tạo JWT token từ thông tin xác thực
            String new_access_token = this.securityUtil.createAccessToken(email, resLoginDTO.getUser());

            resLoginDTO.setAccessToken(new_access_token);

            // Tạo refresh token
            String new_refresh_token = this.securityUtil.createRefreshToken(email, resLoginDTO);

            //update User
            this.userService.updateUserToken(email, new_refresh_token);

            //Set Cookie
            ResponseCookie responseCookie = ResponseCookie.from("refresh_token", new_refresh_token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(resLoginDTO);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout")
    public ResponseEntity<Void> logout(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.handleGetUserByEmail(email);

        this.userService.updateUserToken(email, null);

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(null);
    }
}
