package com.example.web_study.lecture.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web_study.com.annotation.CurrentUser;
import com.example.web_study.lecture.service.LectureService;
import com.example.web_study.lecture.service.dto.LectureDto;
import com.example.web_study.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

	private final LectureService lectureService;

	@PostMapping
	public ResponseEntity<Void> createLecture(
		@RequestBody @Valid LectureDto.Create request,
		@CurrentUser User user
	) {
		lectureService.createLecture(request, user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/apply")
	public ResponseEntity<Void> applyLectures(
		@RequestBody @Valid LectureDto.ApplyRequest request,
		@CurrentUser User user
	) {
		lectureService.applyLectures(request, user);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<Page<LectureDto.Response>> getLectures(
		@RequestParam(defaultValue = "recent") String sort,
		@PageableDefault(size = 20) Pageable pageable
	) {
		Page<LectureDto.Response> response = lectureService.getLectures(sort, pageable);
		return ResponseEntity.ok(response);
	}


}
