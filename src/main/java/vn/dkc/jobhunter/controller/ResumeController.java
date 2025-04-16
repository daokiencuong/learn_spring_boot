package vn.dkc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dkc.jobhunter.domain.Resume;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeCreateDTO;
import vn.dkc.jobhunter.service.ResumeService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;


@RestController
@RequestMapping("/api/${dkc.application.version}")
public class ResumeController {
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new resume")
    public ResponseEntity<ResResumeCreateDTO> createResume(@RequestBody Resume newResume) {
        ResResumeCreateDTO resResumeCreateDTO = this.resumeService.handleCreateResume(newResume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resResumeCreateDTO);
    }

}
