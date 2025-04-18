package vn.dkc.jobhunter.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import vn.dkc.jobhunter.domain.Company;
import vn.dkc.jobhunter.domain.Role;
import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.domain.response.ResUserCreateDTO;
import vn.dkc.jobhunter.domain.response.ResUserGetDTO;
import vn.dkc.jobhunter.domain.response.ResUserUpdateDTO;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.repository.UserRepository;
import vn.dkc.jobhunter.util.error.EmailExsitsException;
import vn.dkc.jobhunter.util.error.IdInvalidException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public ResUserCreateDTO handleCreateUser(User user) throws EmailExsitsException {
        //Check company
        if(user.getCompany() != null){
            Optional<Company> company = this.companyService.companyRepository.findById(user.getCompany().getId());
            if(company.isPresent()){
                user.setCompany(company.get());
            }
        }

        if(this.userRepository.existsByEmail(user.getEmail())){
            throw new EmailExsitsException("Email " + user.getEmail() + " đã tồn tại. Vui lòng sử dụng email khác.");
        }

        if(user.getRole() != null){
            Role role = this.roleService.handleGetARole(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

        User newUser = this.userRepository.save(user);

        return convertToResUserCreateDTO(newUser);
    }

    public ResUserGetDTO handleGetUserById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User not found"));

        return convertToResUserGetDTO(user);
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

        if(resUserUpdateDTO.getCompany() != null){
            Optional<Company> company = this.companyService.companyRepository.findById(resUserUpdateDTO.getCompany().getId());
            if(company.isPresent()){
                user.setCompany(company.get());
            }
        }

        if(resUserUpdateDTO.getRole() != null){
            Role role = this.roleService.handleGetARole(resUserUpdateDTO.getRole().getId());
            user.setRole(role != null ? role : null);
        }

        User newUser = this.userRepository.save(user);

        resUserUpdateDTO.setUpdateAt(newUser.getUpdatedAt());

        if(newUser.getCompany() != null){
            ResUserUpdateDTO.CompanyUser companyUser = new ResUserUpdateDTO.CompanyUser();
            companyUser.setId(newUser.getCompany().getId());
            companyUser.setName(newUser.getCompany().getName());
            resUserUpdateDTO.setCompany(companyUser);
        }

        if(newUser.getRole() != null) {
            ResUserUpdateDTO.RoleUser roleUser = new ResUserUpdateDTO.RoleUser();
            roleUser.setId(newUser.getRole().getId());
            roleUser.setName(newUser.getRole().getName());
            resUserUpdateDTO.setRole(roleUser);
        }

        return resUserUpdateDTO;
    }

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        List<ResUserGetDTO> resUserGetDTOList = pageUser.getContent()
                .stream()
                .map(user -> {
                    return convertToResUserGetDTO(user);
                })
                .toList();

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

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

    @Transactional
    public User handleGetUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        if(user != null && user.getRole() != null) {
            user.getRole().getPermissions().size(); 
        }
        return user;
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

    public ResUserCreateDTO convertToResUserCreateDTO(User user) {
        ResUserCreateDTO resUserCreateDTO = new ResUserCreateDTO();
        resUserCreateDTO.setId(user.getId());
        resUserCreateDTO.setName(user.getName());
        resUserCreateDTO.setEmail(user.getEmail());
        resUserCreateDTO.setAge(user.getAge());
        resUserCreateDTO.setGender(user.getGender());
        resUserCreateDTO.setAddress(user.getAddress());
        resUserCreateDTO.setCreatedAt(user.getCreatedAt());

        if (user.getCompany() != null) {
            ResUserCreateDTO.CompanyUser companyUser = new ResUserCreateDTO.CompanyUser();
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            resUserCreateDTO.setCompany(companyUser);
        }

        if(user.getRole() != null) {
            ResUserCreateDTO.RoleUser roleUser = new ResUserCreateDTO.RoleUser();
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            resUserCreateDTO.setRole(roleUser);
        }

        return resUserCreateDTO;
    }

    public ResUserGetDTO convertToResUserGetDTO(User user) {
        ResUserGetDTO resUserGetDTO = new ResUserGetDTO();
        resUserGetDTO.setId(user.getId());
        resUserGetDTO.setName(user.getName());
        resUserGetDTO.setEmail(user.getEmail());
        resUserGetDTO.setAge(user.getAge());
        resUserGetDTO.setGender(user.getGender());
        resUserGetDTO.setAddress(user.getAddress());
        resUserGetDTO.setCreatedAt(user.getCreatedAt());
        resUserGetDTO.setUpdatedAt(user.getUpdatedAt());

        if (user.getCompany() != null) {
            ResUserGetDTO.CompanyUser companyUser = new ResUserGetDTO.CompanyUser();
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            resUserGetDTO.setCompany(companyUser);
        }

        if(user.getRole() != null) {
            ResUserGetDTO.RoleUser roleUser = new ResUserGetDTO.RoleUser();
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            resUserGetDTO.setRole(roleUser);
        }

        return resUserGetDTO;
    }
}
