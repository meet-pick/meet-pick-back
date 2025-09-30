package org.jpetto.meetpickback.account.accounts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jpetto.meetpickback.account.friends.entity.Friend;
import org.jpetto.meetpickback.calendars.entity.Calendar;
import org.jpetto.meetpickback.global.jpa.BaseEntity;
import org.jpetto.meetpickback.group.members.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Account extends BaseEntity implements UserDetails {
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

    @Column(name = "profile", nullable = false)
    private String profile;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Friend> sentFriends = new ArrayList<>();

    @OneToMany(mappedBy = "friendAccount")
    private List<Friend> receivedFriends = new ArrayList<>();

    @OneToMany(mappedBy = "memberAccount")
    private List<Member> members = new ArrayList<>();

    // Security를 위해 UserDetails 인터페이스 필수 구현 메서드
    // UserDetails = SpringSecurity에서 사용자 인증 정보를 담는 핵심 인터페이스

    // 권한 목록 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 계정 만료 확인 (true = 정상, false = 만료)
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    // 계정 잠금 확인 (true = 정상, false = 잠금)
    @Override
    public boolean isAccountNonLocked() {
        return !isBlock;
    }

    // 비밀번호 만료 확인(true = 정상, false = 만료)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 최종 계정 활성화 확인
    @Override
    public boolean isEnabled() {
        return !isBlock;
    }
}
