package org.jpetto.meetpickback.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jpetto.meetpickback.global.jpa.BaseEntity;

@Entity
@Getter
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Account extends BaseEntity {
    @Column(name = "username", nullable = false)
    private String username; // 유저 ID

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "location")
    private String location;

    @Column(name = "is_block", nullable = false)
    private boolean isBlock;

}
