package org.jpetto.meetpickback.account.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.account.auth.dto.AuthDto;
import org.jpetto.meetpickback.account.auth.service.AuthService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.jpetto.meetpickback.global.utils.AuthCookieUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieUtil authCookieUtil;

    @Operation(
            summary = "회원가입",
            description = "필수 입력 : 아이디, 비밀번호, 닉네임<br> 선택 입력: 지역"
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthDto.SignUpResponse> signup(@Valid @RequestBody AuthDto.SignUpRequest signUpRequest) {
        try {
            AuthDto.SignUpResponse response = authService.signUp(signUpRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "아이디 중복 체크",
            description = "필수 입력 : 아이디"
    )
    @GetMapping("/check-id/{username}")
    public ResponseEntity<AuthDto.UsernameCheckResponse> checkId(
            @Parameter(description = "중복 확인할 아이디", example = "user123")
            @PathVariable String username) {
        AuthDto.UsernameCheckResponse response = authService.checkUsername(username);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "로그인",
            description = "필수 입력 : 아이디, 비밀번호"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthDto.SecureLoginResponse> login(@Valid @RequestBody AuthDto.LoginRequest loginRequest, HttpServletResponse response) {
        AuthDto.LoginResponse loginResponse= authService.login(loginRequest);

        authCookieUtil.setAuthenticationCookies(response, loginResponse.getAccessToken(), loginResponse.getRefreshToken());

        return ResponseEntity.ok(AuthDto.SecureLoginResponse.from(loginResponse));
    }

    @Operation(
            summary = "유저 정보 조회",
            description = "필수 단계 : 로그인(login api 진행 후 쿠키 받기)"
    )
    @GetMapping("/me")
    public ResponseEntity<AuthDto.UserInfoResponse> getCurrentUser(@Parameter(hidden = true) @LoginUser Account loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            AuthDto.UserInfoResponse userInfo = authService.getUserInfo(loginUser.getUsername());
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            log.error("사용자 정보 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    @Operation(
            summary = "로그아웃",
            description = "필수 입력 : 아이디, 비밀번호"
    )
    @DeleteMapping("/logout")
    public ResponseEntity<AuthDto.LogoutResponse> logout(HttpServletResponse response) {
        try {
            authCookieUtil.clearAuthenticationCookies(response);
            return ResponseEntity.ok(AuthDto.LogoutResponse.builder().message("로그아웃 완료").build());
        }catch (Exception e) {log.warn("로그아웃 중 오류 발생: {}", e.getMessage());
            // 오류가 발생해도 쿠키는 삭제
            authCookieUtil.clearAuthenticationCookies(response);
            return ResponseEntity.ok(AuthDto.LogoutResponse.builder().message("로그아웃 완료").build());
        }
    }
}
