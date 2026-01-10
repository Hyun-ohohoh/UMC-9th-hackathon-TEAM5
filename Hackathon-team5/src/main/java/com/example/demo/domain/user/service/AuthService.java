package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.request.SessionRequestDto;
import com.example.demo.domain.user.dto.response.SessionResponseDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SessionResponseDto login(SessionRequestDto request) {
        User user = userRepository.findByNickname(request.getNickname())
                .map(existingUser -> {
                    // 유저가 있다면 비밀번호 확인
                    if(!passwordEncoder.matches(request.getPassword(), existingUser.getPasswordHash())) {
                        throw new BusinessException(ErrorCode.INVALID_PASSWORD);
                    }
                    return existingUser;
                })
                // 없다면 새로운 유저로 자동 가입
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .nickname(request.getNickname())
                            .passwordHash(passwordEncoder.encode(request.getPassword()))
                            .latitude(request.getLat())
                            .longitude(request.getLng())
                            .build();
                    return userRepository.save(newUser);
                });

        String accessToken = jwtTokenProvider.createToken(user.getId());

        return SessionResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }
}
