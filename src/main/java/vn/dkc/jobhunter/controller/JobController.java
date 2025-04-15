package vn.dkc.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dkc.jobhunter.domain.Job;
import vn.dkc.jobhunter.domain.response.Job.ResJobCreateDTO;
import vn.dkc.jobhunter.domain.response.Job.ResJobUpdateDTO;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.service.JobService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/${dkc.application.version}")
public class JobController {
     final private JobService jobService;

     public JobController(JobService jobService) {
         this.jobService = jobService;
     }

     @PostMapping("/jobs")
     @ApiMessage("Create a new job")
     public ResponseEntity<ResJobCreateDTO> createJob (@RequestBody Job job) {
            ResJobCreateDTO createdJob = this.jobService.createJob(job);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
     }

     @PutMapping("/jobs")
     @ApiMessage("Update a job")
     public ResponseEntity<ResJobUpdateDTO> updateJob (@RequestBody Job job) {
            ResJobUpdateDTO updatedJob = this.jobService.updateJob(job);
            return ResponseEntity.status(HttpStatus.OK).body(updatedJob);
     }

     @GetMapping("/jobs")
     @ApiMessage("Get all jobs")
     public ResponseEntity<ResultPaginationDTO> getAllJob(
             @Filter Specification<Job> specification,
             Pageable pageable
     ){
            return ResponseEntity.ok(this.jobService.handleGetAllJob(specification, pageable));
     }

     @DeleteMapping("/jobs/{id}")
     @ApiMessage("Delete a job")
     public ResponseEntity<Void> deleteJob (@PathVariable Long id) {
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.status(HttpStatus.OK).build();
     }

     @GetMapping("jobs/{id}")
     @ApiMessage("Get a job by id")
     public ResponseEntity<Job> getJobById (@PathVariable Long id) {
            Job job = this.jobService.handleGetAJob(id);
            return ResponseEntity.status(HttpStatus.OK).body(job);
     }
}
