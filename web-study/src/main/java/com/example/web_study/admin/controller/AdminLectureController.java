package com.example.web_study.admin.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web_study.com.annotation.CurrentUser;
import com.example.web_study.lecture.service.LectureService;
import com.example.web_study.lecture.service.dto.TopInstructorDto;
import com.example.web_study.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/lectures")
public class AdminLectureController {

	private final LectureService lectureService;

	@Operation(summary = "수익 TOP 10 강사조회", description = "수익이 가장 많은 강사 10명을 조회합니다")
	@GetMapping("/top-revenue-instructors")
	public ResponseEntity<List<TopInstructorDto>> getTopRevenueInstructors(@Parameter(hidden = true) @CurrentUser User user) {
		List<TopInstructorDto> topInstructors = lectureService.getTop10RevenueInstructors();
		return ResponseEntity.ok(topInstructors);
	}
}