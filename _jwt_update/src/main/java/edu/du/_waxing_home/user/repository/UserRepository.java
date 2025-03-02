package edu.du._waxing_home.user.repository;

import edu.du._waxing_home.user.domain.Role;
import edu.du._waxing_home.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // username이 존재하는지 확인하는 메소드
    boolean existsByUsername(String username);

    // username으로 사용자 조회
    Optional<User> findByUsername(String username);

    // userNum과 role로 사용자 조회
    Optional<User> findByUsernameAndRole(String username, Role role);

}