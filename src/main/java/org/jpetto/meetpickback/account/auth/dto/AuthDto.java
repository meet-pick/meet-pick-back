package org.jpetto.meetpickback.account.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

    // request
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUpRequest {
        @Schema(description = "사용자 아이디", example = "user123")
        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 6, max = 20, message = "아이디는 6자 이상 8자 이하여야 합니다.")
        private String username;

        @Schema(description = "비밀번호", example = "password123!")
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
        private String password;

        @Schema(description = "닉네임", example = "user1")
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
        private String nickname;

        @Schema(description = "지역(선택)", example = "서울")
        private String location;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        @Schema(description = "사용자 아이디", example = "user123")
        @NotBlank(message = "아이디는 필수입니다.")
        private String username;

        @Schema(description = "비밀번호", example = "password123!")
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }


    // response
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUpResponse {
        private Long id;
        private String username;
        private String nickname;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String accessToken;   // 서비스 내부에서만 사용
        private String refreshToken;  // 서비스 내부에서만 사용
        private String tokenType;
        private UserInfo userInfo;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UserInfo {
            private Long id;
            private String username;
            private String nickname;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SecureLoginResponse {
        private String tokenType;        // "Bearer"
        private String message;          // "로그인 성공"
        private UserInfo userInfo;       // 사용자 정보
        private Long expiresIn;          // 토큰 만료 시간 (초)

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UserInfo {
            private Long id;
            private String username;
            private String nickname;
        }

        // LoginResponse에서 SecureLoginResponse로 변환
        public static SecureLoginResponse from(LoginResponse loginResponse) {
            return SecureLoginResponse.builder()
                    .tokenType(loginResponse.getTokenType())
                    .message("로그인이 완료되었습니다.")
                    .userInfo(SecureLoginResponse.UserInfo.builder()
                            .id(loginResponse.getUserInfo().getId())
                            .username(loginResponse.getUserInfo().getUsername())
                            .nickname(loginResponse.getUserInfo().getNickname())
                            .build())
                    .expiresIn(15 * 60L) // 30분 (초 단위)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsernameCheckResponse {
        private String username;
        private boolean available;    // true: 사용 가능, false: 이미 존재
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfoResponse {
        private Long id;
        private String username;
        private String nickname;
        private String location;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LogoutResponse {
        private String message;
    }
}
