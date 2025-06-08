package com.example.web_study.lecture.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lecture_applicant_count")
@SQLDelete(sql = "UPDATE lecture_applicant_count SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class LectureApplicantCount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id")
	private Lecture lecture;

	@Column(nullable = false)
	private int applicantCount;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;


	public LectureApplicantCount(Lecture lecture) {
		this.lecture = lecture;
		this.applicantCount = 0;
	}

	public void increase() {
		this.applicantCount++;
	}

	public void decrease() {
		if (this.applicantCount > 0) {
			this.applicantCount--;
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
