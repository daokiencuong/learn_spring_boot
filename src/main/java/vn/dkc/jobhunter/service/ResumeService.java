package vn.dkc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Job;
import vn.dkc.jobhunter.domain.Resume;
import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.request.Resume.ReqResumeUpdateDTO;
import vn.dkc.jobhunter.domain.response.ResUserGetDTO;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeCreateDTO;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeGetDTO;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeUpdateDTO;
import vn.dkc.jobhunter.repository.JobRepository;
import vn.dkc.jobhunter.repository.ResumeRepository;
import vn.dkc.jobhunter.repository.UserRepository;
import vn.dkc.jobhunter.util.error.ResumeException;

import java.util.List;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public ResResumeCreateDTO handleCreateResume(Resume newResume) {
        if(newResume == null) {
            throw new ResumeException("Resume cannot be null");
        }

        User user = newResume.getUser();
        if(!this.userRepository.existsById(user.getId())) {
            throw new ResumeException("User does not exist");
        }

        Job job = newResume.getJob();
        if(!this.jobRepository.existsById(job.getId())) {
            throw new ResumeException("Job does not exist");
        }

        Resume savedResume = this.resumeRepository.save(newResume);

        return new ResResumeCreateDTO(savedResume.getId(), savedResume.getCreatedAt(), savedResume.getCreatedBy());
    }

    public ResResumeUpdateDTO handleUpdateResume(ReqResumeUpdateDTO newResumeUpdate) {
        if(newResumeUpdate == null) {
            throw new ResumeException("Resume cannot be null");
        }

        Resume resume = this.resumeRepository.findById(newResumeUpdate.getId())
                .orElseThrow(() -> new ResumeException("Resume not found"));

        resume.setStatus(newResumeUpdate.getStatus());

        Resume updatedResume = this.resumeRepository.save(resume);

        return new ResResumeUpdateDTO(updatedResume.getUpdatedAt(), updatedResume.getUpdatedBy());
    }

    public void handleDeleteResume(Long id) {
        Resume resume = this.resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeException("Resume not found"));

        this.resumeRepository.delete(resume);
    }

    public ResultPaginationDTO handleGetAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        List<ResResumeGetDTO> resResumeGetDTOList = pageResume.getContent()
                .stream()
                .map(resume -> {
                    return convertToResResumeGetDTO(resume);
                })
                .toList();

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(resResumeGetDTOList);

        return paginationDTO;
    }

    public ResResumeGetDTO handleGetResume(Long id) {
        Resume resume = this.resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeException("Resume not found"));

        return convertToResResumeGetDTO(resume);
    }

    public ResResumeGetDTO convertToResResumeGetDTO(Resume resume) {

        ResResumeGetDTO resResumeGetDTO = new ResResumeGetDTO();
        resResumeGetDTO.setId(resume.getId());
        resResumeGetDTO.setCreatedAt(resume.getCreatedAt());
        resResumeGetDTO.setCreatedBy(resume.getCreatedBy());
        resResumeGetDTO.setUpdatedAt(resume.getUpdatedAt());
        resResumeGetDTO.setUpdatedBy(resume.getUpdatedBy());
        resResumeGetDTO.setStatus(resume.getStatus());

        if(resume.getJob() != null) {
            resResumeGetDTO.setCompanyName(resume.getJob().getCompany().getName());
        }

        User user = resume.getUser();
        ResResumeGetDTO.UserResume userResume = new ResResumeGetDTO.UserResume();
        userResume.setId(user.getId());
        userResume.setName(user.getName());

        resResumeGetDTO.setUser(userResume);

        Job job = resume.getJob();
        ResResumeGetDTO.JobResume jobResume = new ResResumeGetDTO.JobResume();
        jobResume.setId(job.getId());
        jobResume.setName(job.getName());

        resResumeGetDTO.setJob(jobResume);

        return resResumeGetDTO;
    }

}
