package com.example.web_study.auth.controller;

import com.example.web_study.auth.service.AuthService;
import com.example.web_study.auth.service.dto.LoginRequest;
import com.example.web_study.auth.service.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "로그인 요청",
					required = true,
					content = @Content(schema = @Schema(implementation = LoginRequest.class))
			)
			@RequestBody @Valid LoginRequest request
	) {
		return ResponseEntity.ok(authService.login(request));
	}
}

