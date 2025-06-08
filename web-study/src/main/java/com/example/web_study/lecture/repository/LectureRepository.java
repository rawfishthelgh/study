package com.example.web_study.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.web_study.lecture.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureRepositoryCustom {
}
