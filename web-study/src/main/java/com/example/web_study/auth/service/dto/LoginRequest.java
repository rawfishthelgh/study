package com.example.web_study.auth.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequest {

	@NotNull
	private String email;
	@NotNull
	private String password;

}
