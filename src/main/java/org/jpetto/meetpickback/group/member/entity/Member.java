package org.jpetto.meetpickback.group.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.global.jpa.BaseEntity;
import org.jpetto.meetpickback.group.group.entity.Group;
import org.jpetto.meetpickback.group.member.enums.MemberRole;

@Entity
@Table(name = "group_member")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Member extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account memberAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    private MemberRole role;
}
