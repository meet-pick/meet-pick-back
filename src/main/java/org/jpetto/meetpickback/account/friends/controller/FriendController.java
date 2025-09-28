package org.jpetto.meetpickback.account.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.account.friends.dto.FriendDto;
import org.jpetto.meetpickback.account.friends.enums.FriendStatus;
import org.jpetto.meetpickback.account.friends.service.FriendService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @Operation(
            summary = "친구 추가",
            description = "필수 입력 : 친구 AccountId"
    )
    @PostMapping
    public ResponseEntity<FriendDto.friendAddResponse> addFriend(
            @Parameter(hidden = true) @LoginUser Account loginUser,
            @Valid @RequestBody FriendDto.friendAddRequest request
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        log.info("Adding friend {}", request);

        FriendDto.friendAddResponse response = friendService.addFriend(loginUser, request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "친구 조회",
            description = "status -> PENDING(친구 요청 진행중), ACCEPTED(친구인 상태) <br>"
                    + "isSender -> true : 사용자가 요청 / false : 사용자가 받음"
    )
    @GetMapping
    public ResponseEntity<List<FriendDto.friendGetResponse>> getFriend(@Parameter(hidden = true) @LoginUser Account loginUser) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        List<FriendDto.friendGetResponse> responses = friendService.getFriends(loginUser);

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "친구 수락",
            description = "타인이 보낸 친구 요청을 수락함"
    )
    @PatchMapping("/accept/{friendId}")
    public ResponseEntity<FriendDto.friendUpdateStatusResponse> acceptFriend(
            @Parameter(description = "친구 요청 아이디", example = "1") @PathVariable long friendId,
            @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendUpdateStatusResponse response = friendService.updateFriendStatus(loginUser, friendId, FriendStatus.ACCEPTED);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "친구 거절",
            description = "타인이 보낸 친구 요청을 거절함"
    )
    @PatchMapping("/reject/{friendId}")
    public ResponseEntity<FriendDto.friendUpdateStatusResponse> rejectFriend(
            @Parameter(description = "친구 요청 아이디", example = "1") @PathVariable long friendId,
            @Parameter(hidden = true) @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendUpdateStatusResponse response = friendService.updateFriendStatus(loginUser, friendId, FriendStatus.REJECTED);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "친구 취소",
            description = "사용자가 보낸 친구 요청을 취소함"
    )
    @PatchMapping("/cancel/{friendId}")
    public ResponseEntity<FriendDto.friendUpdateStatusResponse> cancelFriend(
            @Parameter(description = "친구 요청 아이디", example = "1") @PathVariable long friendId,
            @Parameter(hidden = true) @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendUpdateStatusResponse response = friendService.updateFriendStatus(loginUser, friendId, FriendStatus.CANCEL);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "친구 삭제",
            description = "친구를 삭제함"
    )
    @DeleteMapping("/{friendId}")
    public ResponseEntity<FriendDto.friendDeleteStatusResponse> deleteFriend(
            @Parameter(description = "친구 요청 아이디", example = "1") @PathVariable long friendId,
            @Parameter(hidden = true) @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendDeleteStatusResponse response = friendService.deleteFriend(loginUser, friendId);

        return ResponseEntity.ok(response);
    }
}
