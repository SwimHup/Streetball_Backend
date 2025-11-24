package com.example.streetball_backend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
     * 새 사용자 생성
     */
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = new User(
                request.getName(),
                request.getLocationLat(),
                request.getLocationLng(),
                request.getHasBall() != null ? request.getHasBall() : false
        );
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    /**
     * 사용자 위치 업데이트 (핵심 기능)
     */
    @Transactional
    public UserResponse updateUserLocation(Integer userId, UserLocationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        user.setLocationLat(request.getLocationLat());
        user.setLocationLng(request.getLocationLng());
        
        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
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

