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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE lecture_application SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class LectureApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "lecture_id")
	private Lecture lecture;

	@Column(name = "users_id")
	private Long userId;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;


	public LectureApplication(Lecture lecture, Long userId) {
		this.lecture = lecture;
		this.userId = userId;
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
