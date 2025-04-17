package vn.dkc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Permission;
import vn.dkc.jobhunter.domain.Role;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.repository.PermissionRepository;
import vn.dkc.jobhunter.repository.RoleRepository;
import vn.dkc.jobhunter.util.error.RoleException;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role handleCreateRole(Role role) {
        List<Long> permissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .toList();
        List<Permission> permissionsList = this.permissionRepository.findByIdIn(permissionIds);
        role.setPermissions(permissionsList);
        return this.roleRepository.save(role);
    }

    public Role handleUpdateRole(Role role) {
        Role existingRole = this.roleRepository.findById(role.getId())
                .orElseThrow(() -> new RoleException("Role not found"));
        List<Long> permissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .toList();
        List<Permission> permissionsList = this.permissionRepository.findByIdIn(permissionIds);
        role.setPermissions(permissionsList);
        role.setCreatedBy(existingRole.getCreatedBy());
        role.setCreatedAt(existingRole.getCreatedAt());
        return this.roleRepository.save(role);
    }

    public void handleDeleteRole(Long id) {
        Role role = this.roleRepository.findById(id)
                .orElseThrow(() -> new RoleException("Role not found"));
        if(role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            role.getPermissions().clear();
        }
        this.roleRepository.delete(role);
    }

    public ResultPaginationDTO handleGetAllRoles(
            Specification<Role> specification,
            Pageable pageable
    ) {
        Page<Role> pageRoles = this.roleRepository.findAll(specification, pageable);
        List<Role> rolesList = pageRoles.getContent();

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setTotal(pageRoles.getTotalElements());
        meta.setPages(pageRoles.getTotalPages());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(rolesList);

        return resultPaginationDTO;
    }
}
