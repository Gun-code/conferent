package com.conferent.repositories.user;

import com.conferent.entities.User;
import com.conferent.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자 조회
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 이름으로 사용자 검색
     */
    List<User> findByNameContainingOrderByName(String name);
    
    /**
     * 역할로 사용자 필터링
     */
    List<User> findByRoleOrderByName(Role role);
    
    /**
     * 이메일 중복 확인
     */
    boolean existsByEmail(String email);
    
    /**
     * 모든 사용자를 이름 순으로 조회
     */
    @Query("SELECT u FROM User u ORDER BY u.name")
    List<User> findAllOrderByName();
} 