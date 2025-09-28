package org.jpetto.meetpickback.group.members.service;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.account.accounts.repository.AccountRepository;
import org.jpetto.meetpickback.group.groups.entity.Group;
import org.jpetto.meetpickback.group.members.dto.MemberDto;
import org.jpetto.meetpickback.group.members.entity.Member;
import org.jpetto.meetpickback.group.members.enums.MemberRole;
import org.jpetto.meetpickback.group.members.repository.MemberRepository;
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
    public MemberDto.addMemberResponse addMembers(Group group, List<Long> userIds) {
        for (Long userId : userIds) {
            addMemberWithRole(group, userId, MemberRole.MEMBER);
        }

        return MemberDto.addMemberResponse.builder().message("멤버 추가 완료").build();
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
