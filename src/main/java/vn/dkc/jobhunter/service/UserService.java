package vn.dkc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.dto.*;
import vn.dkc.jobhunter.repository.UserRepository;
import vn.dkc.jobhunter.util.error.EmailExsitsException;
import vn.dkc.jobhunter.util.error.IdInvalidException;

import java.time.Instant;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCreateDTO handleCreateUser(User user) throws EmailExsitsException {
        if(this.userRepository.existsByEmail(user.getEmail())){
            throw new EmailExsitsException("Email " + user.getEmail() + " đã tồn tại. Vui lòng sử dụng email khác.");
        }
        User newUser = this.userRepository.save(user);
        UserCreateDTO userCreateDTO = new UserCreateDTO();

        userCreateDTO.setId(newUser.getId());
        userCreateDTO.setName(newUser.getName());
        userCreateDTO.setEmail(newUser.getEmail());
        userCreateDTO.setAge(newUser.getAge());
        userCreateDTO.setGender(newUser.getGender());
        userCreateDTO.setAddress(newUser.getAddress());
        userCreateDTO.setCreatedAt(newUser.getCreatedAt());

        return userCreateDTO;
    }

    public UserGetDTO handleGetUserById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User not found"));

        UserGetDTO userGetDTO = new UserGetDTO();

        userGetDTO.setId(user.getId());
        userGetDTO.setName(user.getName());
        userGetDTO.setEmail(user.getEmail());
        userGetDTO.setAge(user.getAge());
        userGetDTO.setGender(user.getGender());
        userGetDTO.setAddress(user.getAddress());

        return userGetDTO;
    }

    public UserUpdateDTO handleUpdateUser(UserUpdateDTO userUpdateDTO) throws IdInvalidException {
        User user = userRepository.findById(userUpdateDTO.getId())
                .orElseThrow(() -> new IdInvalidException("User not found"));

        if (userUpdateDTO.getName() != null) {
            user.setName(userUpdateDTO.getName());
        }

        if (userUpdateDTO.getAddress() != null) {
            user.setAddress(userUpdateDTO.getAddress());
        }

        user.setUpdatedAt(Instant.now());

        this.userRepository.save(user);

        userUpdateDTO.setUpdateAt(user.getUpdatedAt());

        return userUpdateDTO;
    }

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        List<UserGetDTO> userGetDTOList = pageUser.getContent()
                .stream()
                .map(user -> {
                    UserGetDTO userGetDTO = new UserGetDTO();
                    userGetDTO.setId(user.getId());
                    userGetDTO.setName(user.getName());
                    userGetDTO.setEmail(user.getEmail());
                    userGetDTO.setAge(user.getAge());
                    userGetDTO.setGender(user.getGender());
                    userGetDTO.setAddress(user.getAddress());
                    userGetDTO.setCreatedAt(user.getCreatedAt());
                    userGetDTO.setUpdatedAt(user.getUpdatedAt());
                    return userGetDTO;
                })
                .toList();

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(userGetDTOList);

        return paginationDTO;
    }

    public void handleDeleteUser(long id) throws IdInvalidException {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User not found"));
        this.userRepository.deleteById(id);
    }

    public User handleGetUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public User handleGetUserByUsername(String username){
        return this.userRepository.findByName(username);
    }

    public void updateUserToken(String email, String token){
        User user = handleGetUserByEmail(email);
        if(user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

    public User getUserByRefeshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }
}
