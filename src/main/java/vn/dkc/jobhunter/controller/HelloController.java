package vn.dkc.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.dkc.jobhunter.service.error.IdInvalidException;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello World (Hỏi Dân IT & Eric)";
    }
}
