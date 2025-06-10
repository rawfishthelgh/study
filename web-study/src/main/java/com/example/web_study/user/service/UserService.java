package com.example.web_study.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.web_study.user.entity.User;
import com.example.web_study.user.repository.UserRepository;
import com.example.web_study.user.service.dto.UserCreateDto;
import com.example.web_study.user.service.validator.EmailValidator;
import com.example.web_study.user.service.validator.PasswordValidator;
import com.example.web_study.user.service.validator.PhoneNumberValidator;

import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final PhoneNumberValidator phoneNumberValidator;

	private final UserRepository userRepository;

	public User findById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new NoSuchElementException("가입되지 않은 이메일입니다."));
	}

	@Transactional
	public void register(UserCreateDto request) {
		validateName(request);
		validatePassword(request);
		validatePhoneNumber(request);
		validateEmail(request);

		String digest = passwordEncoder.encode(request.getPassword());
		User user = request.toEntity(digest);
		userRepository.save(user);
	}

	private void validateName(UserCreateDto request) {
		if (request.getName() == null || request.getName().isBlank()) {
			throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
		}
	}

	private void validatePhoneNumber(UserCreateDto request) {
		if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
			throw new IllegalArgumentException("휴대폰 번호는 필수입니다.");
		}
		if (!phoneNumberValidator.isValid(request.getPhoneNumber())) {
			throw new IllegalArgumentException("유효하지 않은 휴대폰 번호입니다.");
		}
		if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
			throw new IllegalStateException("이미 등록된 전화번호입니다.");
		}

	}

	private void validatePassword(UserCreateDto request) {
		if (request.getPassword() == null || request.getPassword().isBlank()) {
			throw new IllegalArgumentException("비밀번호는 필수입니다.");
		}
		if (!PasswordValidator.isValid(request.getPassword())){
			throw new IllegalArgumentException("비밀번호가 유효하지 않습니다.");
		}
	}

	private void validateEmail(UserCreateDto request) {
		if (request.getEmail() == null || request.getEmail().isBlank()) {
			throw new IllegalArgumentException("이메일은 필수입니다.");
		}
		if (!EmailValidator.isValid(request.getEmail())) {
			throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalStateException("이미 등록된 이메일입니다");
		}
	}

}
