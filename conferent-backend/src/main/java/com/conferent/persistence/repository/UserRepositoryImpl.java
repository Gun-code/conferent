package com.conferent.persistence.repository;

import com.conferent.business.entity.User;
import com.conferent.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepositoryInterface {
    
    private final UserJpaRepositorySpring userJpaRepository;
    
    public UserRepositoryImpl(UserJpaRepositorySpring userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }
    
    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaEntity.fromDomainEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(entity);
        return savedEntity.toDomainEntity();
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
            .map(UserJpaEntity::toDomainEntity);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
            .map(UserJpaEntity::toDomainEntity);
    }
    
    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
            .map(UserJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }
} 