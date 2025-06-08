package com.example.web_study.lecture.service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.web_study.lecture.entity.Lecture;
import com.example.web_study.user.entity.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LectureDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Create {

		@NotBlank
		private String title;

		@Min(1)
		private int maxStudent;

		@Positive
		private int price;

		public Lecture toEntity(User user) {
			return new Lecture(title, maxStudent, BigDecimal.valueOf(price), user.getId());
		}

	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ApplyRequest {

		private List<Long> lectureIds;

	}
}