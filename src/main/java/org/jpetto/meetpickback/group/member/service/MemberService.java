package org.jpetto.meetpickback.group.member.service;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.account.repository.AccountRepository;
import org.jpetto.meetpickback.group.group.entity.Group;
import org.jpetto.meetpickback.group.group.repository.GroupRepository;
import org.jpetto.meetpickback.group.member.entity.Member;
import org.jpetto.meetpickback.group.member.enums.MemberRole;
import org.jpetto.meetpickback.group.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void addOwner(Group group, long userId) {
        addMemberWithRole(group, userId, MemberRole.OWNER);
    }

    @Transactional
    public void addMembers(Group group, List<Long> userIds) {
        for (Long userId : userIds) {
            addMemberWithRole(group, userId, MemberRole.MEMBER);
        }
    }

    private void addMemberWithRole(Group group, long userId, MemberRole role) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (memberRepository.existsByMemberAccountAndGroup(account, group)) {
            throw new IllegalArgumentException("Member already exists");
        }

        Member member = Member.builder()
                .memberAccount(account)
                .group(group)
                .role(role)
                .build();


        memberRepository.save(member);
        group.getMembers().add(member);
    }

    @Transactional
    public boolean isOwner(Group group, Account account) {
        Member member = memberRepository.findByMemberAccountAndGroup(account, group).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return member.getRole() == MemberRole.OWNER;
    }
}
