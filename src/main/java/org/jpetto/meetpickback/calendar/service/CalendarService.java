package org.jpetto.meetpickback.calendar.service;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.auth.entity.Account;
import org.jpetto.meetpickback.calendar.dto.CalendarDto;
import org.jpetto.meetpickback.calendar.entity.Calendar;
import org.jpetto.meetpickback.calendar.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import static org.jpetto.meetpickback.global.utils.StringUtil.nvl;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

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
}
