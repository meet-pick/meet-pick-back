package org.jpetto.meetpickback.group.members.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.account.accounts.service.AccountService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.jpetto.meetpickback.group.groups.entity.Group;
import org.jpetto.meetpickback.group.groups.service.GroupService;
import org.jpetto.meetpickback.group.members.dto.MemberDto;
import org.jpetto.meetpickback.group.members.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{groupId}/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final GroupService groupService;

    /* 멤버 추가 */
    @PostMapping
    public ResponseEntity<MemberDto.addMemberResponse> addMember(
            @PathVariable long groupId,
            @LoginUser Account loginUser,
            @Valid @RequestBody MemberDto.addMemberRequest request
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        MemberDto.addMemberResponse response = groupService.addMemberToGroup(loginUser, groupId, request);

        return ResponseEntity.ok(response);
    }

    /* 멤버 강퇴 */
    // 강퇴는 추후 추가

    /* 멤버 나가기 (강퇴랑 동일 서비스 사용) */
    @DeleteMapping
    public ResponseEntity<MemberDto.deleteMemberResponse> deleteMember(
            @PathVariable long groupId,
            @LoginUser Account account
    ) {
        if (account == null) {
            throw new SecurityException("You are not logged in");
        }

        MemberDto.deleteMemberResponse response = groupService.deleteMemberToGroup(account, groupId);

        return ResponseEntity.ok(response);
    }

    /* 방장 위임 */
    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberDto.transferOwnershipResponse> transferOwnership(
            @PathVariable long groupId,
            @PathVariable long memberId,
            @LoginUser Account account
    ) {
        if (account == null) {
            throw new SecurityException("You are not logged in");
        }

        MemberDto.transferOwnershipResponse response = groupService.changeOwnership(account, groupId, memberId);

        return ResponseEntity.ok(response);
    }

    /* 멤버 조회 */
    @GetMapping
    public ResponseEntity<MemberDto.getMemberResponse> getMembers(
            @PathVariable long groupId,
            @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        Group group = groupService.getGroupById(groupId);

        MemberDto.getMemberResponse response = memberService.getMembers(loginUser, group);

        return ResponseEntity.ok(response);
    }
}
