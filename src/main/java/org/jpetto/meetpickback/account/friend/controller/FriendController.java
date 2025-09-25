package org.jpetto.meetpickback.account.friend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.friend.dto.FriendDto;
import org.jpetto.meetpickback.account.friend.enums.FriendStatus;
import org.jpetto.meetpickback.account.friend.service.FriendService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    /* 친구 추가 */
    @PostMapping
    public ResponseEntity<FriendDto.friendAddResponse> addFriend(
            @LoginUser Account loginUser,
            @Valid @RequestBody FriendDto.friendAddRequest request
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        log.info("Adding friend {}", request);

        FriendDto.friendAddResponse response = friendService.addFriend(loginUser, request);

        return ResponseEntity.ok(response);
    }

    /* 친구 리스트 조회 */
    @GetMapping
    public ResponseEntity<List<FriendDto.friendGetResponse>> getFriend(@LoginUser Account loginUser) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        List<FriendDto.friendGetResponse> responses = friendService.getFriends(loginUser);

        return ResponseEntity.ok(responses);
    }

    /* 친구 수락 */
    @PatchMapping("/accept/{friendId}")
    public ResponseEntity<FriendDto.friendUpdateStatusResponse> acceptFriend(
            @PathVariable long friendId,
            @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendUpdateStatusResponse response = friendService.updateFriendStatus(loginUser, friendId, FriendStatus.ACCEPTED);

        return ResponseEntity.ok(response);
    }

    /* 친구 거절 */
    @PatchMapping("/reject/{friendId}")
    public ResponseEntity<FriendDto.friendUpdateStatusResponse> rejectFriend(
            @PathVariable long friendId,
            @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendUpdateStatusResponse response = friendService.updateFriendStatus(loginUser, friendId, FriendStatus.REJECTED);

        return ResponseEntity.ok(response);
    }

    /* 친구 취소 */
    @PatchMapping("/cancel/{friendId}")
    public ResponseEntity<FriendDto.friendUpdateStatusResponse> cancelFriend(
            @PathVariable long friendId,
            @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendUpdateStatusResponse response = friendService.updateFriendStatus(loginUser, friendId, FriendStatus.CANCEL);

        return ResponseEntity.ok(response);
    }

    /* 친구 삭제 */
    @DeleteMapping("/{friendId}")
    public ResponseEntity<FriendDto.friendDeleteStatusResponse> deleteFriend(
            @PathVariable long friendId,
            @LoginUser Account loginUser
    ) {
        if (loginUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }

        FriendDto.friendDeleteStatusResponse response = friendService.deleteFriend(loginUser, friendId);

        return ResponseEntity.ok(response);
    }
}
