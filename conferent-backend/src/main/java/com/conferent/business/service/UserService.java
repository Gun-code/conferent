package com.conferent.business.service;

import com.conferent.business.entity.User;
import com.conferent.business.entity.UserRole;
import com.conferent.business.exception.BusinessException;
import com.conferent.persistence.repository.UserRepositoryInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    
    private final UserRepositoryInterface userRepository;
    
    public UserService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(String name, String email, String password, UserRole role) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("이미 존재하는 이메일입니다.");
        }
        
        User user = User.builder()
            .name(name)
            .email(email)
            .password(password)
            .role(role)
            .build();
            
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다."));
    }
    
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다."));
    }
    
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException("존재하지 않는 사용자입니다.");
        }
        userRepository.deleteById(id);
    }
} 