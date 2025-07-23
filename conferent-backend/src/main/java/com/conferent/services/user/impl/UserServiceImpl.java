package com.conferent.services.user.impl;

import com.conferent.dtos.auth.RegisterRequest;
import com.conferent.dtos.user.UserRequest;
import com.conferent.dtos.user.UserResponse;
import com.conferent.entities.User;
import com.conferent.enums.Role;
import com.conferent.exceptions.NotFoundException;
import com.conferent.repositories.user.UserRepository;
import com.conferent.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 관련 비즈니스 로직 구현 클래스
 * 
 * 주요 기능:
 * 1. 사용자 CRUD 작업
 * 2. Spring Security와 연동된 인증 처리
 * 3. 비밀번호 암호화/검증
 * 4. 사용자 검색 및 필터링
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    
    // 사용자 데이터 액세스를 위한 리포지토리
    private final UserRepository userRepository;
    
    // Spring Security의 비밀번호 암호화/검증을 위한 인코더
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 모든 사용자 목록 조회 (읽기 전용)
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.debug("전체 사용자 목록 조회");
        return userRepository.findAllOrderByName()
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
    
    /**
     * ID로 사용자 조회 (읽기 전용)
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.debug("사용자 조회 - ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. ID: " + id));
        return UserResponse.from(user);
    }
    
    /**
     * 이메일로 사용자 조회 (읽기 전용)
     * 로그인 인증 시 주로 사용
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.debug("사용자 조회 - 이메일: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. Email: " + email));
        return UserResponse.from(user);
    }
    
    /**
     * 이름으로 사용자 검색 (읽기 전용)
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsersByName(String name) {
        log.debug("사용자 이름 검색: {}", name);
        return userRepository.findByNameContainingOrderByName(name)
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 역할별 사용자 필터링 (읽기 전용)
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findUsersByRole(Role role) {
        log.debug("역할별 사용자 조회: {}", role);
        return userRepository.findByRoleOrderByName(role)
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자 생성 (기존 메서드 - 평문 비밀번호 저장)
     * 관리자가 사용자를 직접 생성할 때 사용
     */
    @Override
    public UserResponse createUser(UserRequest request) {
        log.info("사용자 생성 - 이메일: {}, 이름: {}", request.getEmail(), request.getName());
        
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: 추후 암호화 적용
        user.setRole(request.getRole());
        
        User savedUser = userRepository.save(user);
        log.info("사용자 생성 완료 - ID: {}, 이메일: {}", savedUser.getId(), savedUser.getEmail());
        
        return UserResponse.from(savedUser);
    }

    /**
     * 회원가입을 위한 사용자 생성 (비밀번호 암호화 포함)
     * 일반 사용자가 회원가입할 때 사용
     */
    @Override
    public UserResponse createUserWithEncryption(RegisterRequest request) {
        log.info("회원가입 - 이메일: {}, 이름: {}", request.getEmail(), request.getName());
        
        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("회원가입 실패 - 이메일 중복: {}", request.getEmail());
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        // 2. 새 사용자 엔티티 생성
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        // 3. 비밀번호 암호화 처리
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        
        user.setRole(request.getRole());
        
        // 4. 데이터베이스에 저장
        User savedUser = userRepository.save(user);
        
        log.info("회원가입 완료 - ID: {}, 이메일: {}, 역할: {}", 
                savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
        
        return UserResponse.from(savedUser);
    }
    
    /**
     * 사용자 정보 수정
     */
    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        log.info("사용자 수정 - ID: {}, 이메일: {}", id, request.getEmail());
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. ID: " + id));
        
        // 이메일이 변경되었고 중복되는지 체크
        if (!existingUser.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        
        // 비밀번호가 변경된 경우에만 암호화 처리
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        
        existingUser.setRole(request.getRole());
        
        User updatedUser = userRepository.save(existingUser);
        log.info("사용자 수정 완료 - ID: {}, 이메일: {}", updatedUser.getId(), updatedUser.getEmail());
        
        return UserResponse.from(updatedUser);
    }

    /**
     * 사용자 삭제
     */
    @Override
    public void deleteUser(Long id) {
        log.info("사용자 삭제 - ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("사용자를 찾을 수 없습니다. ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("사용자 삭제 완료 - ID: {}", id);
    }

    /**
     * 이메일 중복 체크
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 로그인을 위한 사용자 인증
     * Spring Security의 UserDetailsService와 함께 사용됨
     * 
     * @param email 사용자 이메일
     * @param rawPassword 평문 비밀번호
     * @return 인증 성공 시 true, 실패 시 false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean authenticateUser(String email, String rawPassword) {
        log.debug("사용자 인증 시도 - 이메일: {}", email);
        
        try {
            // 1. 이메일로 사용자 조회
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            
            if (user == null) {
                log.debug("사용자 인증 실패 - 존재하지 않는 이메일: {}", email);
                return false;
            }
            
            // 2. 비밀번호 검증
            boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
            
            if (matches) {
                log.debug("사용자 인증 성공 - 이메일: {}", email);
            } else {
                log.debug("사용자 인증 실패 - 비밀번호 불일치: {}", email);
            }
            
            return matches;
            
        } catch (Exception e) {
            log.error("사용자 인증 중 오류 발생 - 이메일: {}, 오류: {}", email, e.getMessage());
            return false;
        }
    }
} 