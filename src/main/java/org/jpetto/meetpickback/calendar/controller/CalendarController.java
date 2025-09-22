package org.jpetto.meetpickback.calendar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.auth.entity.Account;
import org.jpetto.meetpickback.calendar.dto.CalendarDto;
import org.jpetto.meetpickback.calendar.service.CalendarService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    /* 일정 추가 */
    @PostMapping
    public ResponseEntity<CalendarDto.calendarCreateResponse> createCalendar(
            @LoginUser Account loginUser,
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

    @PatchMapping("/{calendarId}")
    /* 일정 수정 */
    public ResponseEntity<CalendarDto.calendarUpdateResponse> updateCalendar(
            @LoginUser Account loginUser,
            @PathVariable long calendarId,
            @Valid @RequestBody CalendarDto.calendarUpdateRequest request) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        CalendarDto.calendarUpdateResponse response = calendarService.updateCalendar(loginUser, calendarId, request);

        return ResponseEntity.ok(response);
    }

    /* 일정 삭제 */
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<CalendarDto.calendarDeleteResponse> deleteCalendar(
            @LoginUser Account loginUser,
            @PathVariable long calendarId) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        CalendarDto.calendarDeleteResponse response = calendarService.deleteCalendar(loginUser, calendarId);

        return ResponseEntity.ok(response);
    }

    /* 일정 조회 */
    @GetMapping
    public ResponseEntity<List<CalendarDto.calendarGetResponse>> getCalendars(
            @LoginUser Account loginUser,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<CalendarDto.calendarGetResponse> responses = calendarService.getCalendar(loginUser, startDate, endDate);

        return ResponseEntity.ok(responses);
    }

}
