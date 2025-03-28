package vn.dkc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.dkc.jobhunter.domain.dto.LoginDTO;

@RestController
public class AuthController {
    //Dêpencency Injection 
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login (@RequestBody LoginDTO loginDTO){
        //Load username and password to Security
        UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        //Authenticate user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return ResponseEntity.status(HttpStatus.OK).body(loginDTO);
    }
}
