package com.example.streetball_backend.User;

import com.example.streetball_backend.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 모든 사용자 조회
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * ID로 사용자 조회
     */
    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        return new UserResponse(user);
    }

    /**
     * 회원가입 (핵심 기능)
     */
    @Transactional
    public UserResponse signup(SignupRequest request) {
        // 이름 중복 확인
        if (userRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이름입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 생성
        User user = new User(
                request.getName(),
                encodedPassword,
                request.getHasBall() != null ? request.getHasBall() : false
        );
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    /**
     * 로그인 (핵심 기능)
     * 로그인 시 위치 정보도 함께 업데이트하고 JWT 토큰 발급
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 사용자 조회
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 위치 정보 업데이트
        if (request.getLocationLat() != null && request.getLocationLng() != null) {
            user.setLocationLat(request.getLocationLat());
            user.setLocationLng(request.getLocationLng());
            userRepository.save(user);
        }

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getUserId(), user.getName());

        return new LoginResponse(user, "로그인 성공", token);
    }

    /**
     * 사용자 정보 업데이트
     */
    @Transactional
    public UserResponse updateUser(Integer userId, UserCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getLocationLat() != null) {
            user.setLocationLat(request.getLocationLat());
        }
        if (request.getLocationLng() != null) {
            user.setLocationLng(request.getLocationLng());
        }
        if (request.getHasBall() != null) {
            user.setHasBall(request.getHasBall());
        }
        
        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}

