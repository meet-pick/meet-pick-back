package org.jpetto.meetpickback.calendar.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class calendarUpdateRequest {
//
//    }

    // response
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class calendarCreateResponse {
        private String message;
    }

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class calendarUpdateResponse {
//
//    }
//
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class calendarDeleteResponse {
//
//    }
}
