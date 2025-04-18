package vn.dkc.jobhunter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vn.dkc.jobhunter.domain.Permission;
import vn.dkc.jobhunter.domain.Role;
import vn.dkc.jobhunter.domain.User;
import vn.dkc.jobhunter.service.UserService;
import vn.dkc.jobhunter.util.SecurityUtil;
import vn.dkc.jobhunter.util.error.IdInvalidException;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor{
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        System.out.println(">> Run preHandle method");
        System.out.println("Request URI: " + requestURI);
        System.out.println("Request Method: " + method);
        System.out.println("Path: " + path);

        //Check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        if(!email.isEmpty()){
            User user = this.userService.handleGetUserByEmail(email);
            if(user!=null){
                Role role = user.getRole();
                if(role!=null){
                    List<Permission> permissions = role.getPermissions();
                    if(permissions!=null && !permissions.isEmpty()){
                        for(Permission permission : permissions){
                            System.out.println("Permission: " + permission.getName());
                            System.out.println("API Path: " + permission.getApiPath());
                            System.out.println("Method: " + permission.getMethod());
                            System.out.println("Path: " + path);
                            System.out.println("Request URI: " + requestURI);
                            System.out.println("Request Method: " + method);
                            if(permission.getApiPath().equals(path) && permission.getMethod().equals(method)){
                                return true;
                            }
                        }
                        throw new IdInvalidException("User does not have permission to access this resource");
                    }else {
                        throw new IdInvalidException("User does not have permission to access this resource");
                    }
                }
            }
        }

        return true;
    }
}
