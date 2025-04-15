package vn.dkc.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dkc.jobhunter.domain.Skill;
import vn.dkc.jobhunter.domain.request.Skill.ReqSkillCreate;
import vn.dkc.jobhunter.domain.request.Skill.ReqSkillUpdate;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.service.SkillService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/${dkc.application.version}")
public class SkillController {
    final private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create new skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody ReqSkillCreate newSkill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.skillService.handleCreateSkill(newSkill)
        );
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> updateSkill(@RequestBody ReqSkillUpdate updateSkill) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.skillService.handleUpdateSkill(updateSkill)
        );
    }

    @GetMapping("/skills")
    @ApiMessage("Get all skills")
    public ResponseEntity<ResultPaginationDTO> getSkills(
            @Filter Specification<Skill> spec,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.skillService.handleGetAllSkills(spec, pageable)
        );
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) {
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
