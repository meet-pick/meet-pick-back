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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<MemberDto.MemberInfo> addMembers(Group group, List<Long> userIds) {
        List<MemberDto.MemberInfo> failMembers = new ArrayList<>();

        for (Long userId : userIds) {
            try {
                addSingleMember(group, userId);
            }catch (Exception e) {
                accountRepository.findById(userId).ifPresent(account ->
                        failMembers.add(MemberDto.MemberInfo.from(account))
                );
            }
        }

        return failMembers;
    }

    // 개별 트랜잭션으로 처리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addSingleMember(Group group, long userId) {
        addMemberWithRole(group, userId, MemberRole.MEMBER);
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

    @Transactional
    public boolean isInGroup(Group group, Account account) {
        return memberRepository.existsByMemberAccountAndGroup(account, group);
    }

    @Transactional
    public void deleteMember(Group group, Account account) {
        Member member = memberRepository.findByMemberAccountAndGroup(account, group).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (member.getRole() == MemberRole.OWNER) {
            throw new IllegalArgumentException("You can't delete owner member");
        }

        memberRepository.delete(member);
    }

    @Transactional
    public void changOwner(Account account, Group group, long memberId) {
        Account memberAccount = accountRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Member Owner = memberRepository.findByMemberAccountAndGroup(account, group).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Member member = memberRepository.findByMemberAccountAndGroup(memberAccount, group).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Owner.setRole(MemberRole.MEMBER);
        member.setRole(MemberRole.OWNER);
    }

    public MemberDto.getMemberResponse getMembers(Account loginUser, Group group) {
        if (!memberRepository.existsByMemberAccountAndGroup(loginUser, group)) {
            throw new IllegalArgumentException("Member not found");
        }

        List<Member> members = memberRepository.findByGroup(group);

        List<MemberDto.MemberInfo> memberInfos = new ArrayList<>();
        for (Member member : members) {
            Account memberAccount = member.getMemberAccount();
            memberInfos.add(MemberDto.MemberInfo.from(memberAccount));
        }

        return MemberDto.getMemberResponse.builder().memberInfoList(memberInfos).message("멤버 정보 조회").build();
    }
}
