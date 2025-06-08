package com.example.web_study.lecture.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.web_study.lecture.service.dto.LectureDto;
import com.example.web_study.lecture.service.dto.LectureSortType;

public interface LectureRepositoryCustom {

	Page<LectureDto.Response> findAllBySort(LectureSortType sort, Pageable pageable);
}
