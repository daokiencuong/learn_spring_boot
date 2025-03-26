package vn.ngotien.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {

    private final HelloController helloController;
    private final UserService userService;

    public UserController(UserService userService, HelloController helloController) {
        this.userService = userService;
        this.helloController = helloController;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User postManUser) {
        User newUser = this.userService.handleCreateUser(postManUser);
        return newUser;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id){
        return this.userService.handleGetUserById(id);
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        return this.userService.handleGetAllUser();
    }

    @PutMapping("/user")
    public User putMethodName(@RequestBody User user) {
        return this.userService.handleUpdateUser(user);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id){
        this.userService.handleDeleteUser(id);
        return "Delete user";
    }

}
