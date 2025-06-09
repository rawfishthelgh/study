package com.example.web_study.lecture.service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopInstructorDto {
	private Long instructorId;
	private String instructorName;
	private BigDecimal totalRevenue;
}

