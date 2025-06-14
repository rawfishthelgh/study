package com.example.web_study.lecture.service.dto;

import com.example.web_study.lecture.entity.Lecture;
import com.example.web_study.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class LectureDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @NotBlank
        private String title;

        @Min(1)
        private int maxStudent;

        @PositiveOrZero
        private int price;

        public Lecture toEntity(User user) {
            return new Lecture(title, maxStudent, BigDecimal.valueOf(price), user.getId());
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplyRequest {

        private List<Long> lectureIds;

    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private BigDecimal price;
        private String instructorName;
        private int applicantCount;
        private int maxStudent;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayRequest {

        private List<PayInfo> payInfos;

        @Getter
        @NoArgsConstructor
        public static class PayInfo {
            private Long lectureId;
            private BigDecimal amount;
            private LocalDateTime paidAt;
        }

    }

}