package vn.dkc.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    final private PermissionRepository permissionRepository;
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
}
