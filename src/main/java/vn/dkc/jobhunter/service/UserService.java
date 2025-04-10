package vn.dkc.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.dto.Meta;
import vn.dkc.jobhunter.domain.dto.ResultPaginationDTO;
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

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(pageUser.getContent());

        return paginationDTO;
    }

    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }

    public User handleGetUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public User handleGetUserByUsername(String username){
        return this.userRepository.findByName(username);
    }
}
