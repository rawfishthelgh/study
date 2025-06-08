package com.example.web_study.lecture.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.example.web_study.lecture.entity.Lecture;
import com.example.web_study.lecture.entity.LectureApplicantCount;
import com.example.web_study.lecture.entity.LectureApplication;
import com.example.web_study.lecture.fixture.ApplyRequestFixture;
import com.example.web_study.lecture.fixture.LectureCreateFixture;
import com.example.web_study.lecture.repository.LectureApplicantCountRepository;
import com.example.web_study.lecture.repository.LectureApplicationRepository;
import com.example.web_study.lecture.repository.LectureRepository;
import com.example.web_study.lecture.service.dto.LectureDto;
import com.example.web_study.user.entity.User;
import com.example.web_study.user.entity.UserType;
import com.example.web_study.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class LectureServiceTest {

	@Autowired private LectureService lectureService;
	@Autowired private LectureRepository lectureRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private LectureApplicationRepository lectureApplicationRepository;
	@Autowired private LectureApplicantCountRepository lectureApplicantCountRepository;


	@Nested
	@DisplayName("Lecture Create Test")
	class LectureCreateTest {
		@Test
		void 강의등록_성공() {
			User instructor = savedInstructor();
			LectureDto.Create request = LectureCreateFixture.valid();
			lectureService.createLecture(request, instructor);
			List<Lecture> lectures = lectureRepository.findAll();
			assertThat(lectures).hasSize(1);
			Lecture saved = lectures.get(0);
			assertThat(saved.getTitle()).isEqualTo("자바 기초");
			assertThat(saved.getUserId()).isEqualTo(instructor.getId());
		}

		@Test
		void 강의명_null이면_예외() {
			User instructor = savedInstructor();
			LectureDto.Create request = LectureCreateFixture.with(null, 10, 50000);
			assertThatThrownBy(() -> lectureService.createLecture(request, instructor))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("강의명은 필수");
		}

		@Test
		void 최대_수강인원_0이면_예외() {
			User instructor = savedInstructor();
			LectureDto.Create request = LectureCreateFixture.with("자바", 0, 50000);
			assertThatThrownBy(() -> lectureService.createLecture(request, instructor))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("최대 수강 인원은 1명 이상");
		}

		@Test
		void 가격이_0미만이면_예외() {
			User instructor = savedInstructor();
			LectureDto.Create request = LectureCreateFixture.with("자바", 10, -10);
			assertThatThrownBy(() -> lectureService.createLecture(request, instructor))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("가격은 0보다 작을 수 없습니다");
		}

		@Test
		void STUDENT는_강의등록_불가() {
			User student = userRepository.save(new User("홍수강", "student@example.com", "01011112222", "pwd", UserType.STUDENT));
			LectureDto.Create request = LectureCreateFixture.valid();
			assertThatThrownBy(() -> lectureService.createLecture(request, student))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("수강생은 강의를 등록할 수 없습니다");
		}
	}

	@Nested
	@DisplayName("Lecture Apply Test")
	class LectureApplyTest {
		@Test
		void 여러_강의_동시_신청_성공() {
			User instructor = saveInstructor();
			lectureService.createLecture(LectureCreateFixture.random(), instructor);
			lectureService.createLecture(LectureCreateFixture.random(), instructor);
			User student = saveStudent("수강생1", "student1@example.com");
			lectureService.applyLectures(
				new LectureDto.ApplyRequest(lectureRepository.findAll().stream().map(Lecture::getId).toList()),
				student
			);
			List<LectureApplication> apps = lectureApplicationRepository.findAll();
			assertThat(apps).hasSize(2);
			assertThat(apps).extracting("userId").containsOnly(student.getId());
		}

		@Test
		void 이미_신청한_강의면_예외() {
			User instructor = saveInstructor();
			lectureService.createLecture(LectureCreateFixture.with("강의", 2, 10000), instructor);
			User student = saveStudent("수강생", "student@example.com");
			Lecture lecture = lectureRepository.findAll().get(0);
			lectureService.applyLectures(ApplyRequestFixture.withIds(List.of(lecture.getId())), student);
			assertThatThrownBy(() -> lectureService.applyLectures(ApplyRequestFixture.withIds(List.of(lecture.getId())), student))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("이미 신청한 강의입니다.");
		}

		@Test
		void 최대_수강인원_초과시_예외() {
			User instructor = saveInstructor();
			lectureService.createLecture(LectureCreateFixture.with("강의", 1, 10000), instructor);
			Lecture lecture = lectureRepository.findAll().get(0);
			User student1 = new User("수강생1", "student1@example.com", "010-1111-2222", "pwd", UserType.STUDENT);
			User student2 = new User("수강생2", "student2@example.com", "010-3333-4444", "pwd", UserType.STUDENT);
			userRepository.saveAll(List.of(student1, student2));
			lectureService.applyLectures(ApplyRequestFixture.withIds(List.of(lecture.getId())), student1);
			assertThatThrownBy(() -> lectureService.applyLectures(ApplyRequestFixture.withIds(List.of(lecture.getId())), student2))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("최대 수강 인원을 초과하여 신청할 수 없습니다");
		}
	}

	@Nested
	@DisplayName("LectureSortType 기준으로 정렬된 강의 목록 조회")
	class GetLecturesSortedTest {
		@BeforeEach
		void setUp() {
			User instructor = saveInstructor();
			Lecture a = saveLectureWith(instructor, "A", 10, 10000); applyStudents(a, 1);
			Lecture b = saveLectureWith(instructor, "B", 10, 10000); applyStudents(b, 5);
			Lecture c = saveLectureWith(instructor, "C", 5, 10000); applyStudents(c, 4);
		}

		@Test
		void 최신순으로_조회된다() {
			Page<LectureDto.Response> lectures = lectureService.getLectures("recent", PageRequest.of(0,10));
			assertThat(lectures).extracting(LectureDto.Response::getTitle).containsExactly("C", "B", "A");
		}

		@Test
		void 신청자_많은_순으로_조회된다() {
			Page<LectureDto.Response> lectures = lectureService.getLectures("popular", PageRequest.of(0,10));
			assertThat(lectures).extracting(LectureDto.Response::getTitle).containsExactly("B", "C", "A");
		}

		@Test
		void 신청률_높은_순으로_조회된다() {
			Page<LectureDto.Response> lectures = lectureService.getLectures("rate", PageRequest.of(0,10));
			assertThat(lectures).extracting(LectureDto.Response::getTitle).containsExactly("C", "B", "A");
		}

		private Lecture saveLectureWith(User instructor, String title, int maxStudent, int price) {
			Lecture lecture = new Lecture(title, maxStudent, BigDecimal.valueOf(price), instructor.getId());
			lectureRepository.save(lecture);
			lectureApplicantCountRepository.save(new LectureApplicantCount(lecture));
			return lecture;
		}

		private void applyStudents(Lecture lecture, int count) {
			for (int i = 0; i < count; i++) {
				String uniquePhone = "010" + lecture.getId() + String.format("%04d", i); // 강의 ID로 prefix
				User student = new User(
					"학생" + lecture.getTitle() + i,
					"s" + i + lecture.getTitle() + "@test.com",
					uniquePhone,
					"pwd",
					UserType.STUDENT
				);
				userRepository.save(student);
				lectureApplicationRepository.save(new LectureApplication(lecture, student.getId()));
				LectureApplicantCount lac = lectureApplicantCountRepository.findById(lecture.getId()).orElseThrow();
				lac.increase();
			}
		}
	}


	private User savedInstructor() {
		return userRepository.save(new User("강사", "instructor@example.com", "01033334444", "pwd", UserType.INSTRUCTOR));
	}

	private User saveStudent(String name, String email) {
		return userRepository.save(new User(name, email, "01012345678", "pwd", UserType.STUDENT));
	}

	private User saveInstructor() {
		return userRepository.save(new User("강사", "instructor@example.com", "01011112222", "pwd", UserType.INSTRUCTOR));
	}


}
