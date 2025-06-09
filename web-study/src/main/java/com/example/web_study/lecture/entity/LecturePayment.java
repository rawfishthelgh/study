package com.example.web_study.lecture.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lecture_payment")
public class LecturePayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "lecture_id", nullable = false)
	private Long lectureId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(name = "paid_at", nullable = false)
	private LocalDateTime paidAt;

	public LecturePayment(Long lectureId, Long userId, BigDecimal amount, LocalDateTime paidAt) {
		this.lectureId = lectureId;
		this.userId = userId;
		this.amount = amount;
		this.paidAt = paidAt;
	}
}