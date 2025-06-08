package com.example.web_study.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.web_study.auth.JwtTokenProvider;
import com.example.web_study.auth.service.dto.LoginRequest;
import com.example.web_study.auth.service.dto.LoginResponse;
import com.example.web_study.user.entity.User;
import com.example.web_study.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public LoginResponse login(LoginRequest request) {
		User user = userService.findByEmail(request.getEmail());

		validatePasswordMatches(request, user);

		String token = jwtTokenProvider.generateToken(
			user.getId(),
			user.getEmail(),
			user.getUserType().name()
		);

		return new LoginResponse("Bearer " + token);
	}

	private void validatePasswordMatches(LoginRequest request, User user) {
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
	}
}

