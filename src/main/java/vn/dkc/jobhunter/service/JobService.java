package vn.dkc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Job;
import vn.dkc.jobhunter.domain.Skill;
import vn.dkc.jobhunter.domain.response.Job.ResJobCreateDTO;
import vn.dkc.jobhunter.domain.response.Job.ResJobUpdateDTO;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.repository.JobRepository;
import vn.dkc.jobhunter.repository.SkillRepository;
import vn.dkc.jobhunter.util.error.ObjectExistsException;

import java.time.Instant;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResJobCreateDTO createJob(Job job) {
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills().stream().map(Skill::getId).toList();
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);

            job.setSkills(dbSkills);
        }

        this.jobRepository.save(job);

        ResJobCreateDTO resJobCreateDTO = new ResJobCreateDTO();

        resJobCreateDTO.setId(job.getId());
        resJobCreateDTO.setName(job.getName());
        resJobCreateDTO.setLocation(job.getLocation());
        resJobCreateDTO.setSalary(job.getSalary());
        resJobCreateDTO.setQuantity(job.getQuantity());
        resJobCreateDTO.setLevel(job.getLevel());
        resJobCreateDTO.setDescription(job.getDescription());
        resJobCreateDTO.setStartDate(job.getStartDate());
        resJobCreateDTO.setEndDate(job.getEndDate());
        resJobCreateDTO.setActive(job.isActive());
        resJobCreateDTO.setCreatedAt(job.getCreatedAt());
        resJobCreateDTO.setCreatedBy(job.getCreatedBy());

        if(job.getSkills() != null){
            List<String> skills = job.getSkills().stream().map(Skill::getName).toList();
            resJobCreateDTO.setSkills(skills);
        }
        else {
            resJobCreateDTO.setSkills(null);
        }

        return resJobCreateDTO;
    }

    public ResJobUpdateDTO updateJob(Job job) {
        Job existingJob = this.jobRepository.findById(job.getId())
                .orElseThrow(() -> new ObjectExistsException("Job not found"));

        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills().stream().map(Skill::getId).toList();
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);

            job.setSkills(dbSkills);
        }
        job.setCreatedAt(existingJob.getCreatedAt());
        job.setCreatedBy(existingJob.getCreatedBy());

        Job savedJob = this.jobRepository.save(job);

        ResJobUpdateDTO resJobUpdateDTO = new ResJobUpdateDTO();

        resJobUpdateDTO.setId(savedJob.getId());
        resJobUpdateDTO.setName(savedJob.getName());
        resJobUpdateDTO.setLocation(savedJob.getLocation());
        resJobUpdateDTO.setSalary(savedJob.getSalary());
        resJobUpdateDTO.setQuantity(savedJob.getQuantity());
        resJobUpdateDTO.setLevel(savedJob.getLevel());
        resJobUpdateDTO.setDescription(savedJob.getDescription());
        resJobUpdateDTO.setStartDate(savedJob.getStartDate());
        resJobUpdateDTO.setEndDate(savedJob.getEndDate());
        resJobUpdateDTO.setActive(savedJob.isActive());
        resJobUpdateDTO.setUpdatedAt(savedJob.getUpdatedAt());
        resJobUpdateDTO.setUpdatedBy(savedJob.getUpdatedBy());

        if(savedJob.getSkills() != null){
            List<String> skills = savedJob.getSkills().stream().map(Skill::getName).toList();
            resJobUpdateDTO.setSkills(skills);
        }
        else {
            resJobUpdateDTO.setSkills(null);
        }

        return resJobUpdateDTO;
    }

    public ResultPaginationDTO handleGetAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJobs = this.jobRepository.findAll(spec, pageable);

        List<Job> jobsList = pageJobs.getContent();

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageJobs.getTotalPages());
        meta.setTotal(pageJobs.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(jobsList);

        return resultPaginationDTO;
    }

    public void handleDeleteJob(Long id) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectExistsException("Job not found"));
        job.getSkills().forEach(skill -> skill.getJobs().remove(job));

        this.jobRepository.delete(job);
    }

    public Job handleGetAJob(Long id) {
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectExistsException("Job not found"));
    }


}
