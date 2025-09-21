package org.jpetto.meetpickback.calendar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.auth.entity.Account;
import org.jpetto.meetpickback.calendar.dto.CalendarDto;
import org.jpetto.meetpickback.calendar.entity.Calendar;
import org.jpetto.meetpickback.calendar.service.CalendarService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            ResponseEntity.status(401).build();
        }

        try {
            CalendarDto.calendarCreateResponse newCalendar =
                    calendarService.createCalendar(loginUser, request);
            return ResponseEntity.ok(newCalendar);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PatchMapping("/{calendarId}")
//    /* 일정 수정 */
//    public ResponseEntity<CalendarDto.calendarUpdateResponse> updateCalendar(
//            @PathVariable long calendarId,
//            @Valid @RequestBody CalendarDto.calendarUpdateRequest request) {
//        return ResponseEntity.ok(null);
//    }
//
//    /* 일정 삭제 */
//    @DeleteMapping("/{calendarId}")
//    public ResponseEntity<CalendarDto.calendarDeleteResponse> deleteCalendar(@PathVariable long calendarId) {
//        return ResponseEntity.ok(null);
//    }

    /* 일정 조회 */
    @GetMapping
    public ResponseEntity<List<CalendarDto>> getCalendars() {
        return ResponseEntity.ok(null);
    }

}
