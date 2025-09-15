package org.jpetto.meetpickback.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpetto.meetpickback.auth.dto.AuthDto;
import org.jpetto.meetpickback.auth.service.AuthService;
import org.jpetto.meetpickback.global.utils.AuthCookieUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieUtil authCookieUtil;

    @PostMapping("/test")
    public String test() {
        return "test";
    }

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

    @GetMapping("/check-id/{username}")
    public ResponseEntity<AuthDto.UsernameCheckResponse> checkId(@PathVariable String username) {
        AuthDto.UsernameCheckResponse response = authService.checkUsername(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto.SecureLoginResponse> login(@Valid @RequestBody AuthDto.LoginRequest loginRequest, HttpServletResponse response) {
        AuthDto.LoginResponse loginResponse= authService.login(loginRequest);

        authCookieUtil.setAuthenticationCookies(response, loginResponse.getAccessToken(), loginResponse.getRefreshToken());

        return ResponseEntity.ok(AuthDto.SecureLoginResponse.from(loginResponse));
    }

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
