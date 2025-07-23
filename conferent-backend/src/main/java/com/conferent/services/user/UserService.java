package com.conferent.services.user;

import com.conferent.dtos.auth.RegisterRequest;
import com.conferent.dtos.user.UserRequest;
import com.conferent.dtos.user.UserResponse;
import com.conferent.enums.Role;

import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 
 * 주요 기능:
 * 1. 사용자 CRUD 작업
 * 2. 사용자 검색 및 필터링
 * 3. 인증을 위한 사용자 정보 검증
 * 4. 비밀번호 암호화 처리
 */
public interface UserService {

    /**
     * 모든 사용자 목록 조회
     * @return 사용자 목록 (이름 순으로 정렬)
     */
    List<UserResponse> getAllUsers();

    /**
     * ID로 사용자 조회
     * @param id 사용자 ID
     * @return 사용자 정보
     * @throws com.conferent.exceptions.NotFoundException 사용자를 찾을 수 없는 경우
     */
    UserResponse getUserById(Long id);

    /**
     * 이메일로 사용자 조회
     * 로그인 시 사용자 인증에 주로 사용됨
     * 
     * @param email 사용자 이메일
     * @return 사용자 정보
     * @throws com.conferent.exceptions.NotFoundException 사용자를 찾을 수 없는 경우
     */
    UserResponse getUserByEmail(String email);

    /**
     * 이름으로 사용자 검색
     * @param name 검색할 이름 (부분 일치)
     * @return 검색된 사용자 목록
     */
    List<UserResponse> searchUsersByName(String name);

    /**
     * 역할별 사용자 필터링
     * @param role 사용자 역할 (USER, ADMIN)
     * @return 해당 역할의 사용자 목록
     */
    List<UserResponse> findUsersByRole(Role role);

    /**
     * 사용자 생성 (기존 메서드)
     * @param request 사용자 생성 요청
     * @return 생성된 사용자 정보
     * @throws IllegalArgumentException 이메일 중복 시
     */
    UserResponse createUser(UserRequest request);

    /**
     * 회원가입을 위한 사용자 생성 (비밀번호 암호화 포함)
     * 회원가입 API에서 사용되며, 비밀번호를 자동으로 암호화하여 저장
     * 
     * @param request 회원가입 요청 데이터
     * @return 생성된 사용자 정보
     * @throws IllegalArgumentException 이메일 중복 시
     */
    UserResponse createUserWithEncryption(RegisterRequest request);

    /**
     * 사용자 정보 수정
     * @param id 수정할 사용자 ID
     * @param request 수정할 사용자 정보
     * @return 수정된 사용자 정보
     * @throws com.conferent.exceptions.NotFoundException 사용자를 찾을 수 없는 경우
     * @throws IllegalArgumentException 이메일 중복 시
     */
    UserResponse updateUser(Long id, UserRequest request);

    /**
     * 사용자 삭제
     * @param id 삭제할 사용자 ID
     * @throws com.conferent.exceptions.NotFoundException 사용자를 찾을 수 없는 경우
     */
    void deleteUser(Long id);

    /**
     * 이메일 중복 체크
     * @param email 확인할 이메일
     * @return 중복되면 true, 그렇지 않으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 로그인을 위한 사용자 인증
     * 이메일과 평문 비밀번호를 받아서 암호화된 비밀번호와 비교
     * 
     * @param email 사용자 이메일
     * @param rawPassword 평문 비밀번호
     * @return 인증 성공 시 true, 실패 시 false
     */
    boolean authenticateUser(String email, String rawPassword);
} 