package org.jpetto.meetpickback.group.group.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.jpetto.meetpickback.group.group.dto.GroupDto;
import org.jpetto.meetpickback.group.group.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDto.createGroupResponse> createGroup(
            @LoginUser Account loginUser,
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
            @LoginUser Account loginUser,
            @PathVariable long groupId,
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
            @LoginUser Account loginUser,
            @PathVariable long groupId
    ) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        GroupDto.deleteGroupResponse response = groupService.deleteGroup(loginUser, groupId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupDto.getGroupResponse>> getGroups(@LoginUser Account loginUser) {
        if (loginUser == null) {
            throw new SecurityException("You are not logged in");
        }

        List<GroupDto.getGroupResponse> responses = groupService.getGroup(loginUser);

        return ResponseEntity.ok(responses);
    }
}
