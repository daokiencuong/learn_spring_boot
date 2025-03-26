package vn.ngotien.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user){
        return this.userRepository.save(user);
    }
}
