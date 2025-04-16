package vn.dkc.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dkc.jobhunter.domain.Job;
import vn.dkc.jobhunter.domain.Resume;
import vn.dkc.jobhunter.domain.request.Resume.ReqResumeUpdateDTO;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeCreateDTO;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeGetDTO;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeUpdateDTO;
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
    public ResponseEntity<ResResumeCreateDTO> createResume(@Valid @RequestBody Resume newResume) {
        ResResumeCreateDTO resResumeCreateDTO = this.resumeService.handleCreateResume(newResume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resResumeCreateDTO);
    }

    @PutMapping("/resumes")
    @ApiMessage("Update an existing resume")
    public ResponseEntity<ResResumeUpdateDTO> updateResume(@RequestBody ReqResumeUpdateDTO reqResumeUpdateDTO) {
        ResResumeUpdateDTO resResumeUpdateDTO = this.resumeService.handleUpdateResume(reqResumeUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resResumeUpdateDTO);
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id) {
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/resumes")
    @ApiMessage("Get all resumes")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(
            @Filter Specification<Resume> specification,
            Pageable pageable) {
        ResultPaginationDTO resultPaginationDTO = this.resumeService.handleGetAllResumes(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get a resume by ID")
    public ResponseEntity<ResResumeGetDTO> getResumeById(@PathVariable("id") Long id) {
        ResResumeGetDTO resResumeGetDTO = this.resumeService.handleGetResume(id);
        return ResponseEntity.status(HttpStatus.OK).body(resResumeGetDTO);
    }
}
