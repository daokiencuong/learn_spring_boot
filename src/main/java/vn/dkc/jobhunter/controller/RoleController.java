package vn.dkc.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dkc.jobhunter.domain.Role;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
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
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role createdRole = this.roleService.handleCreateRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/roles")
    @ApiMessage("Update role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(this.roleService.handleUpdateRole(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) {
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/roles")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(
            @Filter Specification<Role> spec,
            Pageable pageable){
        return ResponseEntity.ok(this.roleService.handleGetAllRoles(spec, pageable));
    }
}
