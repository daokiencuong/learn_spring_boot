package vn.dkc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dkc.jobhunter.domain.Role;
import vn.dkc.jobhunter.service.RoleService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/${dkc.application.version}")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create role")
    public ResponseEntity<Role> createRole(Role role) {
        Role createdRole = this.roleService.handleCreateRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }
}
