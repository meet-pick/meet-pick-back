package org.jpetto.meetpickback.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jpetto.meetpickback.calendar.entity.Calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarDto {

    //request
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarCreateRequest {
        @NotBlank
        private String title;

        private String description;

        @NotNull
        private LocalDateTime startDate;

        @NotNull
        private LocalDateTime endDate;

        @NotBlank
        private String color;

        private String place;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarUpdateRequest {
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String color;
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
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String color;
        private String place;
    }
}
