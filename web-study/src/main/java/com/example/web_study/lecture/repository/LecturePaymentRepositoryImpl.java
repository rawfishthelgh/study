package com.example.web_study.lecture.repository;

import java.util.List;

import com.example.web_study.lecture.entity.QLecture;
import com.example.web_study.lecture.entity.QLecturePayment;
import com.example.web_study.lecture.service.dto.TopInstructorDto;
import com.example.web_study.user.entity.QUser;
import com.example.web_study.user.entity.UserType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LecturePaymentRepositoryImpl implements LecturePaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QLecture lecture = QLecture.lecture;
    QLecturePayment lecturePayment = QLecturePayment.lecturePayment;
    QUser user = QUser.user;

    @Override
    public List<TopInstructorDto> findTop10InstructorsByRevenue() {
        return queryFactory
                .select(Projections.constructor(
                        TopInstructorDto.class,
                        user.id,
                        user.name,
                        lecturePayment.amount.sum().as("totalRevenue")
                ))
                .from(lecturePayment)
                .join(lecture).on(lecturePayment.lectureId.eq(lecture.id))
                .join(user).on(user.id.eq(lecture.userId))
                .where(user.userType.eq(UserType.INSTRUCTOR))
                .groupBy(user.id, user.name)
                .orderBy(lecturePayment.amount.sum().desc())
                .limit(10)
                .fetch();
    }
}

