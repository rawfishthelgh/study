package com.example.web_study.lecture.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.example.web_study.com.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lecture")
@SQLDelete(sql = "UPDATE lecture SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Lecture extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false)
	private int maxStudent;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private int applicantCount;

	@Column(nullable = false, precision = 5, scale = 2)
	private BigDecimal applicationRate;

	@Column(name = "users_id")
	private Long userId;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	public Lecture(String title, int maxStudent, BigDecimal price, Long userId) {
		this.title = title;
		this.maxStudent = maxStudent;
		this.price = price;
		this.applicantCount = 0;
		this.applicationRate = BigDecimal.ZERO;
		this.userId = userId;
	}

	public void increaseApplicantCount() {
		this.applicantCount++;
		updateApplicationRate();
	}

	public void decreaseApplicantCount() {
		if (this.applicantCount > 0) {
			this.applicantCount--;
			updateApplicationRate();
		}
	}

	private void updateApplicationRate() {
		if (this.maxStudent > 0) {
			this.applicationRate = BigDecimal.valueOf(this.applicantCount * 100.0 / this.maxStudent)
				.setScale(2, RoundingMode.HALF_UP);
		} else {
			this.applicationRate = BigDecimal.ZERO;
		}
	}


	public void delete(){
		this.isDeleted = true;
	}

	@PrePersist
	public void prePersist() {
		if (isDeleted == null) {
			isDeleted = false;
		}
	}

}
