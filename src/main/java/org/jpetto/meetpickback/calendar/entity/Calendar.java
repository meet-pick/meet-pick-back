package org.jpetto.meetpickback.calendar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jpetto.meetpickback.auth.entity.Account;
import org.jpetto.meetpickback.global.jpa.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "calendar")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Calendar extends BaseEntity {
    @Column(name = "calendar_title", nullable = false)
    private String title;

    @Column(name = "calendar_description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "color", nullable = false)
    private String color;

    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public void updateCalendar(String title, String description, LocalDateTime startDate, LocalDateTime endDate, String color, String place) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (startDate != null) this.startDate = startDate;
        if (endDate != null) {
            if (startDate != null && endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("종료일은 시작일보다 빠를 수 없습니다.");
            } else if (startDate == null && this.startDate != null && endDate.isBefore(this.startDate)) {
                throw new IllegalArgumentException("종료일은 시작일보다 빠를 수 없습니다.");
            }
            this.endDate = endDate;
        }
        if (color != null) this.color = color;
        if (place != null) this.place = place;
    }
}
