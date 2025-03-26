package vn.ngotien.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngotien.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
