package edu.du._waxing_home.user.repository;

import edu.du._waxing_home.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username이 존재하는지 확인하는 메소드
    boolean existsByUsername(String username);

    // username으로 사용자 조회
    Optional<User> findByUsername(String username);

}
