package com.example.web_study.lecture.service;

import com.example.web_study.lecture.entity.Lecture;
import com.example.web_study.lecture.entity.LectureApplication;
import com.example.web_study.lecture.entity.LecturePayment;
import com.example.web_study.lecture.repository.LectureApplicationRepository;
import com.example.web_study.lecture.repository.LecturePaymentRepository;
import com.example.web_study.lecture.repository.LectureRepository;
import com.example.web_study.lecture.service.dto.LectureDto;
import com.example.web_study.lecture.service.dto.LectureSortType;
import com.example.web_study.lecture.service.dto.TopInstructorDto;
import com.example.web_study.user.entity.User;
import com.example.web_study.user.entity.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

	private final LectureRepository lectureRepository;
	private final LectureApplicationRepository lectureApplicationRepository;
	private final LecturePaymentRepository lecturePaymentRepository;


	@Transactional
	public void createLecture(LectureDto.Create request, User user) {
		validateInstructor(user);
		validateTitle(request.getTitle());
		validateMaxStudent(request.getMaxStudent());
		validatePrice(request.getPrice());
		Lecture lecture = request.toEntity(user);
		lectureRepository.save(lecture);
	}

	private void validateInstructor(User user) {
		if (user.getUserType() != UserType.INSTRUCTOR) {
			throw new IllegalStateException("수강생은 강의를 등록할 수 없습니다.");
		}
	}

	private void validateTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("강의명은 필수입니다.");
		}
	}

	private void validateMaxStudent(int maxStudent) {
		if (maxStudent < 1) {
			throw new IllegalArgumentException("최대 수강 인원은 1명 이상이어야 합니다.");
		}
	}

	private void validatePrice(int price) {
		if (price < 0) {
			throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");
		}
	}

	@Transactional
	public void applyLectures(LectureDto.ApplyRequest request, User user) {
		List<Long> sortedLectureIds = request.getLectureIds().stream()
			.sorted()
			.toList();

		for (Long lectureId : sortedLectureIds) {
			applyLecture(lectureId, user);
		}
	}

	private void applyLecture(Long lectureId, User user) {
		Lecture lecture = lectureRepository.findWithPessimisticLockById(lectureId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));

		validateOverMaxStudents(lecture);
		validateAlreadyApplied(user, lecture);

		LectureApplication application = new LectureApplication(lecture, user.getId());
		lectureApplicationRepository.save(application);

		lecture.increaseApplicantCount();
	}

	private void validateAlreadyApplied(User user, Lecture lecture) {
		boolean alreadyApplied = lectureApplicationRepository.existsByUserIdAndLecture(user.getId(), lecture);
		if (alreadyApplied) {
			throw new IllegalStateException("이미 신청한 강의입니다.");
		}
	}

	private void validateOverMaxStudents(Lecture lecture) {
		if (lecture.getApplicantCount()>= lecture.getMaxStudent()) {
			throw new IllegalStateException("최대 수강 인원을 초과하여 신청할 수 없습니다.");
		}
	}

	public Page<LectureDto.Response> getLectures(String sort, Pageable pageable) {
		return lectureRepository.findAllBySort(LectureSortType.from(sort), pageable);
	}

	public List<TopInstructorDto> getTop10RevenueInstructors() {
		return lecturePaymentRepository.findTop10InstructorsByRevenue();
	}

	@Transactional
	public void payLecture(User user, LectureDto.PayRequest request) {
		for (LectureDto.PayRequest.PayInfo payInfo : request.getPayInfos()) {
			Long lectureId = payInfo.getLectureId();
			Lecture lecture = lectureRepository.findById(lectureId)
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
			lecturePaymentRepository.save(new LecturePayment(lecture.getId(), user.getId(),payInfo.getAmount(),payInfo.getPaidAt()));
		}
	}
}
