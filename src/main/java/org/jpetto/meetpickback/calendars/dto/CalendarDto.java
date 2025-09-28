package org.jpetto.meetpickback.calendars.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CalendarDto {

    //request
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarCreateRequest {
        @NotBlank
        @Schema(description = "타이틀", example = "테스트")
        private String title;

        @Schema(description = "설명(선택)", example = "테스트 설명입니다.")
        private String description;

        @NotNull
        @Schema(description = "시작 날짜", example = "2025-09-01T09:00:00")
        private LocalDateTime startDate;

        @NotNull
        @Schema(description = "종료 날짜", example = "2025-09-02T09:00:00")
        private LocalDateTime endDate;

        @NotBlank
        @Schema(description = "색상", example = "#2EC4B6")
        private String color;

        @Schema(description = "장소(선택)", example = "서울")
        private String place;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarUpdateRequest {
        @Schema(description = "타이틀", example = "테스트")
        private String title;
        @Schema(description = "설명", example = "테스트 설명입니다.")
        private String description;
        @Schema(description = "시작 날짜", example = "2025-09-01T09:00:00")
        private LocalDateTime startDate;
        @Schema(description = "종료 날짜", example = "2025-09-02T09:00:00")
        private LocalDateTime endDate;
        @Schema(description = "색상", example = "#2EC4B6")
        private String color;
        @Schema(description = "장소", example = "서울")
        private String place;
    }

    // response
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarCreateResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarUpdateResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarDeleteResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarGetResponse {
        private long id;
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String color;
        private String place;
    }
}
