package com.conferent.services.user.impl;

import com.conferent.dtos.user.UserRequest;
import com.conferent.dtos.user.UserResponse;
import com.conferent.entities.User;
import com.conferent.enums.Role;
import com.conferent.exceptions.NotFoundException;
import com.conferent.repositories.user.UserRepository;
import com.conferent.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllOrderByName()
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. ID: " + id));
        return UserResponse.from(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. Email: " + email));
        return UserResponse.from(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsersByName(String name) {
        return userRepository.findByNameContainingOrderByName(name)
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findUsersByRole(Role role) {
        return userRepository.findByRoleOrderByName(role)
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserResponse createUser(UserRequest request) {
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
        return UserResponse.from(savedUser);
    }
    
    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. ID: " + id));
        
        // 이메일이 변경되었고 중복되는지 체크
        if (!existingUser.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword()); // TODO: 추후 암호화 적용
        existingUser.setRole(request.getRole());
        
        User updatedUser = userRepository.save(existingUser);
        return UserResponse.from(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("사용자를 찾을 수 없습니다. ID: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsUser(Long id) {
        return userRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
} 