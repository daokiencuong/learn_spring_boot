package vn.dkc.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dkc.jobhunter.repository.SkillRepository;
import vn.dkc.jobhunter.service.SkillService;

@RestController
@RequestMapping("/api/${dkc.application.version}")
public class SkillController {
    final private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
}
