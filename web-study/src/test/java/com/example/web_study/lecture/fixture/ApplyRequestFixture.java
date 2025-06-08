package com.example.web_study.lecture.fixture;

import java.util.List;

import com.example.web_study.lecture.service.dto.LectureDto;

public class ApplyRequestFixture {

	public static LectureDto.ApplyRequest withIds(List<Long> ids) {
		return new LectureDto.ApplyRequest(ids);
	}
}
