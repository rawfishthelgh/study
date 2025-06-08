package com.example.web_study.lecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web_study.lecture.entity.Lecture;

import jakarta.persistence.LockModeType;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureRepositoryCustom {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT l FROM Lecture l WHERE l.id = :lectureId")
	Optional<Lecture> findWithPessimisticLockById(@Param("lectureId") Long lectureId);

}
