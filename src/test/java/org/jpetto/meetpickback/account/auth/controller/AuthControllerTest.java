package org.jpetto.meetpickback.account.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jpetto.meetpickback.account.auth.dto.AuthDto;
import org.jpetto.meetpickback.account.auth.service.AuthService;
import org.jpetto.meetpickback.global.utils.AuthCookieUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthCookieUtil authCookieUtil;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    // 각 테스트 전에 MockMvc 설정
    private void setupMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("회원가입 API 성공")
    void signup_Success() throws Exception {
        // Given
        setupMockMvc();

        AuthDto.SignUpRequest request = AuthDto.SignUpRequest.builder()
                .username("testuser")
                .password("password123")
                .nickname("테스트유저")
                .location("서울")
                .build();

        AuthDto.SignUpResponse response = AuthDto.SignUpResponse.builder()
                .id(1L)
                .username("testuser")
                .nickname("테스트유저")
                .message("회원가입이 완료되었습니다.")
                .build();

        when(authService.signUp(any(AuthDto.SignUpRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.nickname").value("테스트유저"))
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
                .andDo(print());

        verify(authService).signUp(any(AuthDto.SignUpRequest.class));
    }

    @Test
    @DisplayName("로그인 API 성공")
    void login_Success() throws Exception {
        // Given
        setupMockMvc();

        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .tokenType("Bearer")
                .userInfo(AuthDto.LoginResponse.UserInfo.builder()
                        .id(1L)
                        .username("testuser")
                        .nickname("테스트유저")
                        .build())
                .build();

        when(authService.login(any(AuthDto.LoginRequest.class))).thenReturn(loginResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.message").value("로그인이 완료되었습니다."))
                .andExpect(jsonPath("$.userInfo.id").value(1L))
                .andExpect(jsonPath("$.userInfo.username").value("testuser"))
                .andExpect(jsonPath("$.userInfo.nickname").value("테스트유저"))
                .andDo(print());

        verify(authService).login(any(AuthDto.LoginRequest.class));
        verify(authCookieUtil).setAuthenticationCookies(any(), eq("accessToken"), eq("refreshToken"));
    }

    @Test
    @DisplayName("로그아웃 API 성공")
    void logout_Success() throws Exception {
        // Given
        setupMockMvc();

        // When & Then
        mockMvc.perform(delete("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그아웃 완료"))
                .andDo(print());

        verify(authCookieUtil).clearAuthenticationCookies(any());
    }

    @Test
    @DisplayName("회원가입 API - 유효성 검증 실패")
    void signup_ValidationFail() throws Exception {
        // Given
        setupMockMvc();

        AuthDto.SignUpRequest request = AuthDto.SignUpRequest.builder()
                .username("ab") // 너무 짧음 (6자 미만)
                .password("123") // 너무 짧음 (8자 미만)
                .nickname("") // 빈값
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // 유효성 검증 실패로 서비스 호출되지 않음을 확인
        verify(authService, never()).signUp(any());
    }

    @Test
    @DisplayName("아이디 중복 확인 API 성공")
    void checkUsername_Success() throws Exception {
        // Given
        setupMockMvc();
        String username = "testuser";

        AuthDto.UsernameCheckResponse response = AuthDto.UsernameCheckResponse.builder()
                .username(username)
                .available(true)
                .message("사용 가능한 아이디입니다.")
                .build();

        when(authService.checkUsername(username)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/auth/check-id/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.message").value("사용 가능한 아이디입니다."))
                .andDo(print());

        verify(authService).checkUsername(username);
    }
}