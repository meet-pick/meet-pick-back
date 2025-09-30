package org.jpetto.meetpickback.group.groups.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.jpetto.meetpickback.group.groups.dto.GroupDto;
import org.jpetto.meetpickback.group.groups.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @Operation(
            summary = "그룹 생성",
            description = "필수 입력 : 이름 <br>" +
                    "선택입력 : 설명, 멤버 AccountId"
    )
    @PostMapping
    public ResponseEntity<GroupDto.createGroupResponse> createGroup(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @Valid @RequestBody GroupDto.createGroupRequest request
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        GroupDto.createGroupResponse response = groupService.createGroup(loginUser, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupDto.updateGroupResponse> updateGroup(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @Parameter(description = "수정할 그룹 id", example = "1") @PathVariable long groupId,
            @Valid @RequestBody GroupDto.updateGroupRequest request
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        GroupDto.updateGroupResponse response = groupService.updateGroup(loginUser, groupId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<GroupDto.deleteGroupResponse> deleteGroup(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @Parameter(description = "수정할 그룹 id", example = "1") @PathVariable long groupId
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        GroupDto.deleteGroupResponse response = groupService.deleteGroup(loginUser, groupId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupDto.getGroupResponse>> getGroups(@Parameter(hidden = true) @LoginUser Account loginUser) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        List<GroupDto.getGroupResponse> responses = groupService.getGroup(loginUser);

        return ResponseEntity.ok(responses);
    }
}
