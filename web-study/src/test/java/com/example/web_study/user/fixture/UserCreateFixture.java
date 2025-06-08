package com.example.web_study.user.fixture;

import com.example.web_study.user.service.dto.UserDto;

public class UserCreateFixture {

	public static UserDto.Create valid() {
		return new UserDto.Create(
			"홍길동",
			"hong@example.com",
			"01012345678",
			"Abc123",
			"student"
		);
	}

	public static UserDto.Create with(
		String name,
		String email,
		String phoneNumber,
		String password,
		String userType
	) {
		return new UserDto.Create(name, email, phoneNumber, password, userType);
	}
}

