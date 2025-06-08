package com.example.web_study.lecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web_study.lecture.entity.LectureApplicantCount;

import jakarta.persistence.LockModeType;

public interface LectureApplicantCountRepository extends JpaRepository<LectureApplicantCount, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select c from LectureApplicantCount c where c.lecture.id = :lectureId")
	Optional<LectureApplicantCount> findWithPessimisticLockByLectureId(@Param("lectureId") Long lectureId);
}
