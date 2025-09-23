package org.jpetto.meetpickback.account.account.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.auth.dto.AuthDto;

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
