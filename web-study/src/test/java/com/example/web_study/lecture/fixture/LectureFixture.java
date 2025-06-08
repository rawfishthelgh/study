package com.example.web_study.lecture.fixture;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.web_study.lecture.entity.Lecture;
import com.example.web_study.user.entity.User;

public class LectureFixture {
	private static final AtomicInteger sequence = new AtomicInteger(1);

	public static Lecture create(User instructor, int max) {
		int index = sequence.getAndIncrement();
		return new Lecture(
			"동시신청강의-" + index,
			max,
			BigDecimal.valueOf(10000 + index),
			instructor.getId()
		);
	}

}
