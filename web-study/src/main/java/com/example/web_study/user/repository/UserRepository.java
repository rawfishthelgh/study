package com.example.web_study.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.web_study.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Boolean existsByPhoneNumber(String phoneNumber);
	Boolean existsByEmail(String email);
}
