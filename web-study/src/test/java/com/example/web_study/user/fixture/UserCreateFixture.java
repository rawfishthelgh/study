package com.example.web_study.user.fixture;

import com.example.web_study.user.service.dto.UserCreateDto;

public class UserCreateFixture {

	public static UserCreateDto valid() {
		return new UserCreateDto(
			"홍길동",
			"hong@example.com",
			"01012345678",
			"Abc123",
			"student"
		);
	}

	public static UserCreateDto with(
		String name,
		String email,
		String phoneNumber,
		String password,
		String userType
	) {
		return new UserCreateDto(name, email, phoneNumber, password, userType);
	}
}

