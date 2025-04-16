package vn.dkc.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dkc.jobhunter.service.RoleService;

@RestController
@RequestMapping("/api/${dkc.application.version}")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
}
