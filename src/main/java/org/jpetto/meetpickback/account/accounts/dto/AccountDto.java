package org.jpetto.meetpickback.account.accounts.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountDto {

    // response
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountMyPageResponse {
        private String username;
        private String nickname;
        private String location;
        private String message;
    }

}
