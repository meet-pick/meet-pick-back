package org.jpetto.meetpickback.group.members.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jpetto.meetpickback.account.accounts.entity.Account;

import java.util.List;

public class MemberDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class addMemberRequest {
        @Schema(description = "추가할 멤버 Id 리스트", example = "[1, 2, 3]")
        @NotEmpty
        private List<Long> memberIds;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class addMemberResponse {
        private List<MemberInfo> failMember;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class deleteMemberResponse {
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class transferOwnershipResponse {
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class getMemberResponse {
        private List<MemberInfo> memberInfoList;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberInfo {
        private Long id;
        private String username;
        private String nickname;

        public static MemberInfo from(Account account) {
            return MemberInfo.builder()
                    .id(account.getId())
                    .username(account.getUsername())
                    .nickname(account.getNickname())
                    .build();
        }
    }
}
