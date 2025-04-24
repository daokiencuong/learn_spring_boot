package vn.dkc.jobhunter.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Job;
import vn.dkc.jobhunter.domain.Skill;
import vn.dkc.jobhunter.domain.Subscriber;
import vn.dkc.jobhunter.domain.response.email.ResEmailJob;
import vn.dkc.jobhunter.repository.JobRepository;
import vn.dkc.jobhunter.repository.SubscriberRepository;
import vn.dkc.jobhunter.util.error.SubscriberException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;
    private final EmailService emailService;
    private final JobRepository jobRepository;
    public SubscriberService(SubscriberRepository subscriberRepository, SkillService skillService, EmailService emailService, JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
        this.emailService = emailService;
        this.jobRepository = jobRepository;
    }

    public Subscriber handleCreateSubscriber(Subscriber subscriber) {
        if(isSubscriberExistByEmail(subscriber.getEmail())) {
            throw new SubscriberException("Email already exists");
        }
        if(!subscriber.getSkills().isEmpty()) {
            List<Long> skillIds = subscriber.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
            List<Skill> skills = this.skillService.getSkillsByIds(skillIds);
            subscriber.setSkills(skills);
        }

        return subscriberRepository.save(subscriber);
    }

    public Subscriber handleUpdateSubscriber(Subscriber subscriber) {
        if(!isSubscriberExistById(subscriber.getId())) {
            throw new SubscriberException("Subscriber not found");
        }

        Subscriber existingSubscriber = this.subscriberRepository.findById(subscriber.getId())
                .orElseThrow(() -> new SubscriberException("Subscriber not found"));

        if(!subscriber.getSkills().isEmpty()) {
            List<Long> skillIds = subscriber.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
            List<Skill> skills = this.skillService.getSkillsByIds(skillIds);
            existingSubscriber.setSkills(skills);
        }

        return subscriberRepository.save(existingSubscriber);
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                         List<ResEmailJob> arr = listJobs.stream().map(
                                 job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr
                        );
                    }
                }
            }
        }
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob resEmailJob = new ResEmailJob();
        resEmailJob.setName(job.getName());
        resEmailJob.setSalary(job.getSalary());
        resEmailJob.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<ResEmailJob.SkillEmail> skills = job.getSkills().stream()
                .map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        resEmailJob.setSkills(skills);
        return resEmailJob;
    }

    public boolean isSubscriberExistById(long id) {
        return this.subscriberRepository.existsById(id);
    }

    public boolean isSubscriberExistByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }
}
