package com.example.web_study.user.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.example.web_study.com.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 100, unique = true)
	private String email;

	@Column(nullable = false, length = 20, unique = true)
	private String phoneNumber;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UserType userType;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;


	public User(String name, String email, String phoneNumber, String password, UserType userType) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.userType = userType;
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
