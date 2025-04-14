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

    public ResUserCreateDTO handleCreateUser(User user) throws EmailExsitsException {
        if(this.userRepository.existsByEmail(user.getEmail())){
            throw new EmailExsitsException("Email " + user.getEmail() + " đã tồn tại. Vui lòng sử dụng email khác.");
        }
        User newUser = this.userRepository.save(user);
        ResUserCreateDTO resUserCreateDTO = new ResUserCreateDTO();

        resUserCreateDTO.setId(newUser.getId());
        resUserCreateDTO.setName(newUser.getName());
        resUserCreateDTO.setEmail(newUser.getEmail());
        resUserCreateDTO.setAge(newUser.getAge());
        resUserCreateDTO.setGender(newUser.getGender());
        resUserCreateDTO.setAddress(newUser.getAddress());
        resUserCreateDTO.setCreatedAt(newUser.getCreatedAt());

        return resUserCreateDTO;
    }

    public ResUserGetDTO handleGetUserById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User not found"));

        ResUserGetDTO resUserGetDTO = new ResUserGetDTO();

        resUserGetDTO.setId(user.getId());
        resUserGetDTO.setName(user.getName());
        resUserGetDTO.setEmail(user.getEmail());
        resUserGetDTO.setAge(user.getAge());
        resUserGetDTO.setGender(user.getGender());
        resUserGetDTO.setAddress(user.getAddress());

        return resUserGetDTO;
    }

    public ResUserUpdateDTO handleUpdateUser(ResUserUpdateDTO resUserUpdateDTO) throws IdInvalidException {
        User user = userRepository.findById(resUserUpdateDTO.getId())
                .orElseThrow(() -> new IdInvalidException("User not found"));

        if (resUserUpdateDTO.getName() != null) {
            user.setName(resUserUpdateDTO.getName());
        }

        if (resUserUpdateDTO.getAddress() != null) {
            user.setAddress(resUserUpdateDTO.getAddress());
        }

        user.setUpdatedAt(Instant.now());

        this.userRepository.save(user);

        resUserUpdateDTO.setUpdateAt(user.getUpdatedAt());

        return resUserUpdateDTO;
    }

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        List<ResUserGetDTO> resUserGetDTOList = pageUser.getContent()
                .stream()
                .map(user -> {
                    ResUserGetDTO resUserGetDTO = new ResUserGetDTO();
                    resUserGetDTO.setId(user.getId());
                    resUserGetDTO.setName(user.getName());
                    resUserGetDTO.setEmail(user.getEmail());
                    resUserGetDTO.setAge(user.getAge());
                    resUserGetDTO.setGender(user.getGender());
                    resUserGetDTO.setAddress(user.getAddress());
                    resUserGetDTO.setCreatedAt(user.getCreatedAt());
                    resUserGetDTO.setUpdatedAt(user.getUpdatedAt());
                    return resUserGetDTO;
                })
                .toList();

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(resUserGetDTOList);

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
