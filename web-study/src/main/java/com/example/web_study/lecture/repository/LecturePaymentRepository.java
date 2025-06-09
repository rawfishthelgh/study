package com.example.web_study.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.web_study.lecture.entity.LecturePayment;

public interface LecturePaymentRepository extends JpaRepository<LecturePayment, Long>, LecturePaymentRepositoryCustom {
}
