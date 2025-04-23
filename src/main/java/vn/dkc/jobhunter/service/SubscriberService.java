package vn.dkc.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Skill;
import vn.dkc.jobhunter.domain.Subscriber;
import vn.dkc.jobhunter.repository.SubscriberRepository;
import vn.dkc.jobhunter.util.error.SubscriberException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;
    public SubscriberService(SubscriberRepository subscriberRepository, SkillService skillService) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
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

    public boolean isSubscriberExistById(long id) {
        return this.subscriberRepository.existsById(id);
    }

    public boolean isSubscriberExistByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }
}
