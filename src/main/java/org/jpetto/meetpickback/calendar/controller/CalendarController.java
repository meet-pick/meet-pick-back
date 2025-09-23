package org.jpetto.meetpickback.calendar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.calendar.dto.CalendarDto;
import org.jpetto.meetpickback.calendar.service.CalendarService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @Operation(
            summary = "캘린더 이벤트 추가",
            description = "필수 입력 : 타이틀, 시작 날짜, 종료 날짜, 색상 <br> 선택 입력: 설명, 지역"
    )
    @PostMapping
    public ResponseEntity<CalendarDto.calendarCreateResponse> createCalendar(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @Valid @RequestBody CalendarDto.calendarCreateRequest request) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            CalendarDto.calendarCreateResponse newCalendar =
                    calendarService.createCalendar(loginUser, request);
            return ResponseEntity.ok(newCalendar);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "캘린더 이벤트 수정",
            description = "선택 입력 : 타이틀, 설명, 시작 날짜, 종료 날짜, 색상, 지역"
    )
    @PatchMapping("/{calendarId}")
    public ResponseEntity<CalendarDto.calendarUpdateResponse> updateCalendar(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @Parameter(description = "캘린더 아이디", example = "1") @PathVariable long calendarId,
            @Valid @RequestBody CalendarDto.calendarUpdateRequest request) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        CalendarDto.calendarUpdateResponse response = calendarService.updateCalendar(loginUser, calendarId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "캘린더 이벤트 삭제",
            description = "선택 입력 : 타이틀, 설명, 시작 날짜, 종료 날짜, 색상, 지역"
    )
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<CalendarDto.calendarDeleteResponse> deleteCalendar(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @Parameter(description = "캘린더 아이디", example = "1") @PathVariable long calendarId) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        CalendarDto.calendarDeleteResponse response = calendarService.deleteCalendar(loginUser, calendarId);

        return ResponseEntity.ok(response);
    }

    /* 일정 조회 */
    @GetMapping
    public ResponseEntity<List<CalendarDto.calendarGetResponse>> getCalendars(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "조회 시작 날짜", example = "2025-09-01T09:00:00") @RequestParam LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "조회 종료 날짜", example = "2025-09-30T09:00:00") @RequestParam LocalDateTime endDate
    ) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<CalendarDto.calendarGetResponse> responses = calendarService.getCalendar(loginUser, startDate, endDate);

        return ResponseEntity.ok(responses);
    }

}
