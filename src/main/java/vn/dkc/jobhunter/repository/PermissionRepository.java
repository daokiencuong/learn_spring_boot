package vn.dkc.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.dkc.jobhunter.domain.Permission;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    boolean existsByName(String permissionName);
    boolean existsById(Long permissionId);

    List<Permission> findByName(String name);
    List<Permission> findByIdIn(List<Long> listIds);
}
