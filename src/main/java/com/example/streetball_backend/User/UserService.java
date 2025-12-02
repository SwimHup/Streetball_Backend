package com.example.streetball_backend.User;

import com.example.streetball_backend.Game.GameResponse;
import com.example.streetball_backend.Participation.Participation;
import com.example.streetball_backend.Participation.ParticipationRepository;
import com.example.streetball_backend.Participation.ParticipationRole;
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

    @Autowired
    private ParticipationRepository participationRepository;

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

    /**
     * 사용자가 참여한 진행 중인 게임 조회
     */
    public List<GameResponse> getOngoingGames(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        return participationRepository.findOngoingGamesByUserId(userId).stream()
                .map(p -> new GameResponse(p.getGame()))
                .collect(Collectors.toList());
    }

    /**
     * 사용자가 참여한 과거 게임 조회
     */
    public List<GameResponse> getPastGames(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        return participationRepository.findPastGamesByUserId(userId).stream()
                .map(p -> new GameResponse(p.getGame()))
                .collect(Collectors.toList());
    }

    /**
     * 게임 참여 취소 (진행 중인 게임만)
     */
    @Transactional
    public void cancelGameParticipation(Integer userId, Integer gameId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId);
        }

        Participation participation = participationRepository.findByGameIdAndUserId(gameId, userId)
                .orElseThrow(() -> new RuntimeException("참여 내역을 찾을 수 없습니다."));

        // 게임이 종료된 경우 취소 불가
        if (participation.getGame().getStatus() == com.example.streetball_backend.Game.GameStatus.게임_종료) {
            throw new RuntimeException("종료된 게임은 취소할 수 없습니다.");
        }

        // player인 경우 current_players 감소는 트리거가 처리
        participationRepository.delete(participation);
    }
}

