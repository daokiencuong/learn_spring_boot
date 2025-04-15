package vn.dkc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Skill;
import vn.dkc.jobhunter.domain.request.Skill.ReqSkillCreate;
import vn.dkc.jobhunter.domain.request.Skill.ReqSkillUpdate;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.repository.SkillRepository;
import vn.dkc.jobhunter.util.error.SkillExsitsException;

import java.time.Instant;
import java.util.List;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(ReqSkillCreate newSkill) {
        if(newSkill.getName() == null || isSkillExist(newSkill.getName())) {
            throw new SkillExsitsException("Skill name cannot be null or skill already exists...");
        }
        Skill skill = new Skill();
        skill.setName(newSkill.getName());
        skill.setCreatedAt(Instant.now());
        this.skillRepository.save(skill);

        return skill;
    }

    public Skill handleUpdateSkill(ReqSkillUpdate updateSkill) {
        Skill skill = this.skillRepository.findById(updateSkill.getId())
                .orElseThrow(() -> new SkillExsitsException("Skill not found"));
        if(updateSkill.getName() == null || isSkillExist(updateSkill.getName())) {
            throw new SkillExsitsException("Skill name already exists...");
        }
        skill.setName(updateSkill.getName());
        this.skillRepository.save(skill);

        return skill;
    }

    public ResultPaginationDTO handleGetAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkills = this.skillRepository.findAll(spec, pageable);

        List<Skill> skillsList = pageSkills.getContent();

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageSkills.getTotalPages());
        meta.setTotal(pageSkills.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(skillsList);

        return resultPaginationDTO;
    }

    public void handleDeleteSkill(Long id) {
        Skill skill = this.skillRepository.findById(id)
                .orElseThrow(() -> new SkillExsitsException("Skill not found"));
        skill.getJobs().forEach(job -> job.getSkills().remove(skill));

        this.skillRepository.delete(skill);
    }

    public boolean isSkillExist(String name) {
        return this.skillRepository.existsByName(name);
    }
}
