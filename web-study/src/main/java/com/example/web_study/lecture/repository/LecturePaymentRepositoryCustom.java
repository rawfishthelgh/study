package com.example.web_study.lecture.repository;

import java.util.List;

import com.example.web_study.lecture.service.dto.TopInstructorDto;

public interface LecturePaymentRepositoryCustom {

	List<TopInstructorDto> findTop10InstructorsByRevenue();

}
