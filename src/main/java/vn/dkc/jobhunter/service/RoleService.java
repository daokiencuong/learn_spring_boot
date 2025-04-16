package vn.dkc.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Role;
import vn.dkc.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role handleCreateRole(Role role) {
        return this.roleRepository.save(role);
    }
}
