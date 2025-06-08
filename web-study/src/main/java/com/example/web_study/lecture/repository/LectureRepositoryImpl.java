package com.example.web_study.lecture.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.web_study.lecture.entity.QLecture;
import com.example.web_study.lecture.entity.QLectureApplicantCount;
import com.example.web_study.lecture.service.dto.LectureDto;
import com.example.web_study.lecture.service.dto.LectureSortType;
import com.example.web_study.user.entity.QUser;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QLecture lecture = QLecture.lecture;
	QUser user = QUser.user;
	QLectureApplicantCount applicantCount = QLectureApplicantCount.lectureApplicantCount;

	@Override
	public Page<LectureDto.Response> findAllBySort(LectureSortType sort, Pageable pageable) {
		List<LectureDto.Response> contents = queryFactory
			.select(Projections.constructor(
				LectureDto.Response.class,
				lecture.id,
				lecture.title,
				lecture.price,
				user.name.as("instructorName"),
				applicantCount.applicantCount,
				lecture.maxStudent
			))
			.from(lecture)
			.join(user)
			.on(lecture.userId.eq(user.id))
			.leftJoin(applicantCount)
			.on(applicantCount.lecture.eq(lecture))
			.orderBy(sortOrder(sort))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(lecture.count())
			.from(lecture)
			.fetchOne();

		return new PageImpl<>(contents, pageable, total);
	}

	private OrderSpecifier<?> sortOrder(LectureSortType sort) {
		if (sort == LectureSortType.POPULAR) {
			return applicantCount.applicantCount.desc();
		}

		if (sort == LectureSortType.RATE) {
			return applicantCount.applicantCount
				.doubleValue()
				.divide(lecture.maxStudent.castToNum(Double.class))
				.desc();
		}

		return lecture.createdAt.desc();
	}
}
