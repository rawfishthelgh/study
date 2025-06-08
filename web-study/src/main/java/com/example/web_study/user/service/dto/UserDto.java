package com.example.web_study.user.service.dto;

import com.example.web_study.user.entity.User;
import com.example.web_study.user.entity.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Create {
		@NotBlank
		private String name;
		@Email
		@NotBlank
		private String email;
		@NotBlank
		private String phoneNumber;
		@Size(min = 6, max = 10)
		@NotBlank
		private String password;
		@NotBlank
		private String userType;

		public User toEntity(String digest) {
			return new User(name, email, phoneNumber, digest, UserType.from(userType));
		}
	}
}
