package vn.dkc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Permission;
import vn.dkc.jobhunter.domain.Role;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.repository.PermissionRepository;
import vn.dkc.jobhunter.util.error.PermissionException;

import java.util.List;

@Service
public class PermissionService {
    final private PermissionRepository permissionRepository;
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handleCreatePermission(Permission permission) {
        if(permission == null) {
            throw new PermissionException("Permission cannot be null");
        }
        if(isPermissionExist(permission.getName())){
            checkPermissionExist(permission);
        }
        return permissionRepository.save(permission);
    }

    public Permission handleUpdatePermission(Permission permission) {
        if(
                permission == null ||
                !this.permissionRepository.existsById(permission.getId())
        ) {
            throw new PermissionException("Permission does not exist");
        } else {
            Permission existingPermission = this.permissionRepository.findById(permission.getId()).get();
            permission.setCreatedAt(existingPermission.getCreatedAt());
            permission.setCreatedBy(existingPermission.getCreatedBy());
        }
        if(isPermissionExist(permission.getName())){
            checkPermissionExist(permission);
        }
        return permissionRepository.save(permission);
    }

    public void checkPermissionExist(Permission permission) {
        List<Permission> permissionsList = permissionRepository.findByName(permission.getName());
        permissionsList.stream().forEach(p -> {
            Permission existingPermission = p;
            if(existingPermission.getApiPath().equals(permission.getApiPath()) &&
                    existingPermission.getMethod().equals(permission.getMethod()) &&
                    existingPermission.getModule().equals(permission.getModule()) &&
                    existingPermission.getName().equals(permission.getName())) {
                throw new PermissionException("Permission already exists");
            }
        });
    }

    public ResultPaginationDTO handleGetAllPermissions(
            Specification<Permission> spec,
            Pageable pageable
    ) {
        Page<Permission> permissionsPage = this.permissionRepository.findAll(spec, pageable);
        List<Permission> permissionsList = permissionsPage.getContent();

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(permissionsPage.getTotalPages());
        meta.setTotal(permissionsPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(permissionsList);

        return resultPaginationDTO;

    }

    public void handleDeletePermission(Long id) {
        if(!this.permissionRepository.existsById(id)) {
            throw new PermissionException("Permission does not exist");
        }

        List<Role> roles = this.permissionRepository.findById(id).get().getRoles();
        roles.stream().forEach(role -> {
            role.getPermissions().remove(this.permissionRepository.findById(id).get());
        });

        this.permissionRepository.deleteById(id);
    }

    public boolean isPermissionExist(String permissionName) {
        return permissionRepository.existsByName(permissionName);
    }
}
