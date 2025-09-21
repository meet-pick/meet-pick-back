package org.jpetto.meetpickback.calendar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = "place", nullable = false)
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
