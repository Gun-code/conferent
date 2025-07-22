package com.conferent.services.user;

import com.conferent.dtos.user.UserRequest;
import com.conferent.dtos.user.UserResponse;
import com.conferent.enums.Role;

import java.util.List;

public interface UserService {
    
    /**
     * 모든 사용자 조회
     */
    List<UserResponse> getAllUsers();
    
    /**
     * ID로 사용자 조회
     */
    UserResponse getUserById(Long id);
    
    /**
     * 이메일로 사용자 조회
     */
    UserResponse getUserByEmail(String email);
    
    /**
     * 사용자 이름으로 검색
     */
    List<UserResponse> searchUsersByName(String name);
    
    /**
     * 역할로 사용자 필터링
     */
    List<UserResponse> findUsersByRole(Role role);
    
    /**
     * 사용자 생성
     */
    UserResponse createUser(UserRequest request);
    
    /**
     * 사용자 정보 수정
     */
    UserResponse updateUser(Long id, UserRequest request);
    
    /**
     * 사용자 삭제
     */
    void deleteUser(Long id);
    
    /**
     * 사용자 존재 여부 확인
     */
    boolean existsUser(Long id);
    
    /**
     * 이메일 중복 확인
     */
    boolean existsByEmail(String email);
} 