package org.jpetto.meetpickback.account.friend.service;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.account.repository.AccountRepository;
import org.jpetto.meetpickback.account.friend.dto.FriendDto;
import org.jpetto.meetpickback.account.friend.entity.Friend;
import org.jpetto.meetpickback.account.friend.enums.FriendStatus;
import org.jpetto.meetpickback.account.friend.repository.FriendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public FriendDto.friendAddResponse addFriend(Account loginUser, FriendDto.friendAddRequest request) {
        if (request.getFriendId() == loginUser.getId()) {
            throw new IllegalArgumentException("cannot add yourself as a friend");
        }

        Account friendAccount = accountRepository.findById(request.getFriendId()).orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        if (friendRepository.existsBetween(friendAccount, loginUser)) {
            throw new IllegalArgumentException("Friend already exists");
        }

        Friend friend = Friend.builder().account(loginUser).friendAccount(friendAccount).status(FriendStatus.PENDING).build();

        friendRepository.save(friend);

        return FriendDto.friendAddResponse.builder().message("친구 추가 요청 완료").build();
    }

    public List<FriendDto.friendGetResponse> getFriends(Account loginUser) {
        List<Friend> friendList = friendRepository.findAllFriendsOf(loginUser);

        List<FriendDto.friendGetResponse> responses = new ArrayList<>();

        for (Friend friend : friendList) {
            if (!friend.getAccount().getId().equals(loginUser.getId())) {
                responses.add(dtoConvert(friend, false));
            } else {
                responses.add(dtoConvert(friend, true));
            }
        }

        return responses;
    }

    private FriendDto.friendGetResponse dtoConvert(Friend friend, boolean isSender) {
        Account target = isSender ? friend.getFriendAccount() : friend.getAccount();

        return FriendDto.friendGetResponse.builder()
                .id(friend.getId())
                .username(target.getUsername())
                .nickname(target.getNickname())
                .status(friend.getStatus())
                .isSender(isSender)
                .build();
    }

    @Transactional
    public FriendDto.friendUpdateStatusResponse updateFriendStatus(Account loginUser, long friendId, FriendStatus status) {
        // 고려해야할 부분
        // 1. 해당 엔티티가 Pending 상태여야함
        // 2. loginUser가 수락, 거절 -> friend, 취소 -> account 여야함

        Friend friend = friendRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        if (friend.getStatus() != FriendStatus.PENDING) {
            throw new IllegalArgumentException("Friend request already processed");
        }

        switch (status) {
            case ACCEPTED -> {
                validateRecipient(friend, loginUser.getId());
                friend.setStatus(status);
            }
            case REJECTED -> {
                validateRecipient(friend, loginUser.getId());
                friendRepository.delete(friend);
            }
            case CANCEL -> {
                validateRequester(friend, loginUser.getId());
                friendRepository.delete(friend);
            }
            default -> {
                throw new IllegalArgumentException("Invalid friend status");
            }
        }

        return FriendDto.friendUpdateStatusResponse.builder().message("친구 상태가 변경되었습니다.").build();
    }

    private void validateRecipient(Friend friend, Long loginUserId) {
        if (!loginUserId.equals(friend.getFriendAccount().getId())) {
            throw new IllegalArgumentException("Only the recipient can handle this request");
        }
    }

    private void validateRequester(Friend friend, Long loginUserId) {
        if (!loginUserId.equals(friend.getAccount().getId())) {
            throw new IllegalArgumentException("Only the requester can cancel this request");
        }
    }

    @Transactional
    public FriendDto.friendDeleteStatusResponse deleteFriend(Account loginUser, long friendId) {
        Friend friend = friendRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        // 로그인 유저가 둘 중에 하나라도 포함이 안되면 삭제 불가
        if (!friend.getFriendAccount().getId().equals(loginUser.getId()) && !friend.getAccount().getId().equals(loginUser.getId())) {
            throw new IllegalArgumentException("Friend does not exist");
        }

        friendRepository.delete(friend);

        return FriendDto.friendDeleteStatusResponse.builder().message("친구 삭제가 완료되었습니다.").build();
    }
}
