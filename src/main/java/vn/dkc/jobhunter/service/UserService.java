package vn.dkc.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user){
        return this.userRepository.save(user);
    }

    public User handleGetUserById(long id){
        return this.userRepository.findById(id).orElse(null);
    }

    public User handleUpdateUser(User user){
        return this.userRepository.save(user);
    }

    public List<User> handleGetAllUser(){
        return this.userRepository.findAll();
    }

    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }
}
