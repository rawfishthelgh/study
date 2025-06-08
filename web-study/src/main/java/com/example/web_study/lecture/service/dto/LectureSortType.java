package com.example.web_study.lecture.service.dto;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LectureSortType {
	RECENT("recent"),
	POPULAR("popular"),
	RATE("rate");

	private final String type;

	public static LectureSortType from(String type) {
		return Arrays.stream(values())
			.filter(e -> e.type.equalsIgnoreCase(type))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("잘못된 정렬 조건입니다: " + type));
	}

}
