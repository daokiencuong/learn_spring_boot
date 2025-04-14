package vn.dkc.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dkc.jobhunter.service.JobService;

@RestController
@RequestMapping("/api/${dkc.application.version}")
public class JobController {
     final private JobService jobService;

     public JobController(JobService jobService) {
         this.jobService = jobService;
     }
}
