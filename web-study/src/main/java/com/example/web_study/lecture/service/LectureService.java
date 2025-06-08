package com.example.web_study.lecture.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.web_study.lecture.entity.Lecture;
import com.example.web_study.lecture.entity.LectureApplicantCount;
import com.example.web_study.lecture.entity.LectureApplication;
import com.example.web_study.lecture.repository.LectureApplicantCountRepository;
import com.example.web_study.lecture.repository.LectureApplicationRepository;
import com.example.web_study.lecture.repository.LectureRepository;
import com.example.web_study.lecture.service.dto.LectureDto;
import com.example.web_study.lecture.service.dto.LectureSortType;
import com.example.web_study.user.entity.User;
import com.example.web_study.user.entity.UserType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

	private final LectureRepository lectureRepository;

	@Transactional
	public void createLecture(LectureDto.Create request, User user) {
		validateInstructor(user);
		validateTitle(request.getTitle());
		validateMaxStudent(request.getMaxStudent());
		validatePrice(request.getPrice());

		Lecture lecture = request.toEntity(user);
		lectureRepository.save(lecture);
	}

	private void validateInstructor(User user) {
		if (user.getUserType() != UserType.INSTRUCTOR) {
			throw new IllegalStateException("수강생은 강의를 등록할 수 없습니다.");
		}
	}

	private void validateTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("강의명은 필수입니다.");
		}
	}

	private void validateMaxStudent(int maxStudent) {
		if (maxStudent < 1) {
			throw new IllegalArgumentException("최대 수강 인원은 1명 이상이어야 합니다.");
		}
	}

	private void validatePrice(int price) {
		if (price < 0) {
			throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
		}
	}
}
