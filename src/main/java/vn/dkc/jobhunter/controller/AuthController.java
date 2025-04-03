package vn.dkc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.dkc.jobhunter.domain.dto.LoginDTO;
import vn.dkc.jobhunter.domain.dto.ResLoginDTO;
import vn.dkc.jobhunter.util.SecurityUtil;

@RestController
public class AuthController {
    //Dependency Injection 
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login (@Valid @RequestBody LoginDTO loginDTO){
        //Load username and password to Security
        UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        //Authenticate user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //Create a token
        String access_token = this.securityUtil.createToken(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccessToken(access_token);

        return ResponseEntity.status(HttpStatus.OK).body(resLoginDTO);
    }
}
