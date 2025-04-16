package vn.dkc.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Job;
import vn.dkc.jobhunter.domain.Resume;
import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.response.Resume.ResResumeCreateDTO;
import vn.dkc.jobhunter.repository.JobRepository;
import vn.dkc.jobhunter.repository.ResumeRepository;
import vn.dkc.jobhunter.repository.UserRepository;
import vn.dkc.jobhunter.util.error.ResumeException;

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

}
