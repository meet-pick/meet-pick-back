package org.jpetto.meetpickback.account.friend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.friend.enums.FriendStatus;
import org.jpetto.meetpickback.global.jpa.BaseEntity;

@Entity
@Getter @Setter
@Table(name = "friend")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Friend extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Account friendAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private FriendStatus status;
}
