package com.example.web_study.lecture.fixture;

import java.util.concurrent.atomic.AtomicInteger;

import com.example.web_study.lecture.service.dto.LectureDto;

public class LectureCreateFixture {

	private static final AtomicInteger counter = new AtomicInteger(1);

	public static LectureDto.Create valid() {
		return new LectureDto.Create("자바 기초", 30, 100000);
	}

	public static LectureDto.Create with(String title, int maxStudent, int price) {
		return new LectureDto.Create(title, maxStudent, price);
	}

	public static LectureDto.Create random() {
		int seq = counter.getAndIncrement();
		return new LectureDto.Create(
			"강의제목-" + seq,
			10000 + seq,
			10 + seq
		);
	}
}
