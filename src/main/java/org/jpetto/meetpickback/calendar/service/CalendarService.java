package org.jpetto.meetpickback.calendar.service;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.calendar.dto.CalendarDto;
import org.jpetto.meetpickback.calendar.entity.Calendar;
import org.jpetto.meetpickback.calendar.repository.CalendarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Transactional
    public CalendarDto.calendarCreateResponse createCalendar(Account loginUser, CalendarDto.calendarCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null");
        }

        Calendar calendar = Calendar.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .color(request.getColor())
                .place(request.getPlace())
                .account(loginUser)
                .build();

        calendarRepository.save(calendar);

        return CalendarDto.calendarCreateResponse.builder().message("캘린더 이벤트 생성 완료").build();
    }

    @Transactional
    public CalendarDto.calendarUpdateResponse updateCalendar(Account loginUser, long calendarId, CalendarDto.calendarUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null");
        }

        if (calendarId <= 0) {
            throw new IllegalArgumentException("Invalid calendar id");
        }

        Calendar oldCalendar = calendarRepository.findById(calendarId).orElseThrow(() -> new IllegalArgumentException("Invalid calendar id: " + calendarId));

        if (!oldCalendar.getAccount().equals(loginUser)) {
            throw new IllegalArgumentException("Account is not logged in");
        }

        oldCalendar.updateCalendar(
                request.getTitle(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate(),
                request.getColor(),
                request.getPlace()
        );

        return CalendarDto.calendarUpdateResponse.builder().message("캘린더 이벤트 수정완료").build();
    }

    @Transactional
    public CalendarDto.calendarDeleteResponse deleteCalendar(Account loginUser, long calendarId) {
        if (calendarId <= 0) {
            throw new IllegalArgumentException("Invalid calendar id");
        }

        Calendar calendar = calendarRepository.findById(calendarId).orElseThrow(() -> new IllegalArgumentException("Invalid calendar id: " + calendarId));

        if (!calendar.getAccount().equals(loginUser)) {
            throw new IllegalArgumentException("Account is not logged in");
        }

        calendarRepository.delete(calendar);

        return CalendarDto.calendarDeleteResponse.builder().message("캘린더 이벤트 삭제 완료").build();
    }

    public List<CalendarDto.calendarGetResponse> getCalendar(Account loginUser, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Request is null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date is after end date");
        }

        List<Calendar> calendars = calendarRepository.findByAccountIdAndStartDateBetween(
                loginUser.getId(),
                startDate,
                endDate
        );

        List<CalendarDto.calendarGetResponse> calendarList = new ArrayList<>();
        for (Calendar calendar : calendars) {
            calendarList.add(
                    CalendarDto.calendarGetResponse.builder()
                            .id(calendar.getId())
                            .title(calendar.getTitle())
                            .description(calendar.getDescription())
                            .startDate(calendar.getStartDate())
                            .endDate(calendar.getEndDate())
                            .color(calendar.getColor())
                            .place(calendar.getPlace())
                            .build()
            );
        }

        return calendarList;
    }
}
