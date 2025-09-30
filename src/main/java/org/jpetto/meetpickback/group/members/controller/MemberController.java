package org.jpetto.meetpickback.group.members.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
            summary = "그룹 멤버 추가",
            description = "필수 입력 : 그룹Id, 멤버IdList"
    )
    @PostMapping
    public ResponseEntity<MemberDto.addMemberResponse> addMember(
            @Parameter(description = "멤버를 추가할 그룹 id", example = "1") @PathVariable long groupId,
            @Parameter(hidden = true) @LoginUser Account loginUser,
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

    @Operation(
            summary = "그룹 나가기",
            description = "필수 입력 : 그룹Id"
    )
    @DeleteMapping
    public ResponseEntity<MemberDto.deleteMemberResponse> deleteMember(
            @Parameter(description = "나갈 그룹 id", example = "1") @PathVariable long groupId,
            @Parameter(hidden = true) @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        MemberDto.deleteMemberResponse response = groupService.deleteMemberToGroup(loginUser, groupId);

        return ResponseEntity.ok(response);
    }

    /* 방장 위임 */
    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberDto.transferOwnershipResponse> transferOwnership(
            @Parameter(description = "내가 속한 그룹 id", example = "1") @PathVariable long groupId,
            @Parameter(description = "방장을 위임할 멤버 id", example = "2") @PathVariable long memberId,
            @Parameter(hidden = true) @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        MemberDto.transferOwnershipResponse response = groupService.changeOwnership(loginUser, groupId, memberId);

        return ResponseEntity.ok(response);
    }

    /* 멤버 조회 */
    @GetMapping
    public ResponseEntity<MemberDto.getMemberResponse> getMembers(
            @Parameter(description = "멤버를 조회할 그룹 id", example = "1") @PathVariable long groupId,
            @Parameter(hidden = true) @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        Group group = groupService.getGroupById(groupId);

        MemberDto.getMemberResponse response = memberService.getMembers(loginUser, group);

        return ResponseEntity.ok(response);
    }
}
