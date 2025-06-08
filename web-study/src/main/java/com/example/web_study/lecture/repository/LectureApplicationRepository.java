package com.example.web_study.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.web_study.lecture.entity.Lecture;
import com.example.web_study.lecture.entity.LectureApplication;

public interface LectureApplicationRepository extends JpaRepository<LectureApplication, Long> {

	boolean existsByUserIdAndLecture(Long userId, Lecture lecture);
}
