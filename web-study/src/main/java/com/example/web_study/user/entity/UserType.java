package com.example.web_study.user.entity;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType {
	STUDENT("student"),
	INSTRUCTOR("instructor");

	private final String type;

	public static UserType from(String type) {
		return Arrays.stream(values())
			.filter(userType -> userType.type.equalsIgnoreCase(type))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원 유형입니다: " + type));
	}

}
