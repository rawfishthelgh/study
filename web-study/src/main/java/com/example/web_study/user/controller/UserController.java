package com.example.web_study.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web_study.user.service.UserService;
import com.example.web_study.user.service.dto.UserCreateDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원가입", description = "회원가입합니다 ")
	@PostMapping
	public ResponseEntity<Void> registerUser(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "회원 가입 요청",
					required = true,
					content = @Content(schema = @Schema(implementation = UserCreateDto.class))
			)
			@RequestBody @Valid UserCreateDto request) {
		userService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
