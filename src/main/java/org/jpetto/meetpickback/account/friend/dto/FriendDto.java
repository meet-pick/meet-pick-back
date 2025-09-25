package org.jpetto.meetpickback.account.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jpetto.meetpickback.account.friend.enums.FriendStatus;

public class FriendDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class friendAddRequest {
        private long friendId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class friendAddResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class friendGetResponse {
        private long id;
        private String username;
        private String nickname;
        private FriendStatus status;
        private boolean isSender; // true 시 사용자가 친구 요청 발송자
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class friendUpdateStatusResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class friendDeleteStatusResponse {
        private String message;
    }
}
