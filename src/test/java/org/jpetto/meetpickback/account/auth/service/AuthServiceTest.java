package org.jpetto.meetpickback.account.auth.service;

import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.account.repository.AccountRepository;
import org.jpetto.meetpickback.account.auth.dto.AuthDto;
import org.jpetto.meetpickback.global.utils.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;  // 추가
import org.mockito.quality.Strictness;  // 추가
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)  // 추가: 불필요한 스터빙 경고 무시
class AuthServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUp_Success() {
        // Given
        AuthDto.SignUpRequest request = AuthDto.SignUpRequest.builder()
                .username("testuser")
                .password("password123")
                .nickname("테스트유저")
                .location("서울")
                .build();

        // Mock 설정
        when(accountRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // save 메서드 Mock - 저장된 Account를 캡처해서 확인
        Account savedAccountWithId = TestAccountBuilder.createAccountWithId(
                1L, "testuser", "encodedPassword", "테스트유저", "서울", false);

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account inputAccount = invocation.getArgument(0);

            // 디버깅: 실제로 저장되는 Account 확인
            System.out.println("=== Save 호출된 Account ===");
            System.out.println("Username: " + inputAccount.getUsername());
            System.out.println("Password: " + inputAccount.getPassword());
            System.out.println("Nickname: " + inputAccount.getNickname());
            System.out.println("Location: " + inputAccount.getLocation());
            System.out.println("IsBlock: " + inputAccount.isBlock());

            // ID가 설정된 Account 반환
            return savedAccountWithId;
        });

        // When
        AuthDto.SignUpResponse response = authService.signUp(request);

        // Then - 디버깅 출력 추가
        System.out.println("=== Response 내용 ===");
        System.out.println("Response ID: " + response.getId());
        System.out.println("Response Username: " + response.getUsername());
        System.out.println("Response Nickname: " + response.getNickname());
        System.out.println("Response Message: " + response.getMessage());

        // 검증
        assertThat(response.getId()).isNotNull(); // 일단 null이 아닌지만 확인
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getNickname()).isEqualTo("테스트유저");
        assertThat(response.getMessage()).contains("회원가입");

        // Mock 호출 검증
        verify(accountRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("password123");
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 아이디")
    void signUp_Fail_DuplicateUsername() {
        // Given
        AuthDto.SignUpRequest request = AuthDto.SignUpRequest.builder()
                .username("existuser")
                .password("password123")
                .nickname("테스트유저")
                .build();

        when(accountRepository.existsByUsername("existuser")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");

        verify(accountRepository).existsByUsername("existuser");
        // 중복일 때는 이후 로직이 실행되지 않음을 확인
        verify(passwordEncoder, never()).encode(anyString());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_Success() {
        // Given
        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        // Account 엔티티 Mock
        Account mockAccount = Account.builder()
                .username("testuser")
                .nickname("테스트유저")
                .build();

        // UserDetails Mock
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testuser");

        // Authentication Mock
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(mockUserDetails);

        // Mock 설정 - 실제 호출되는 것만 설정
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(accountRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(mockAccount));
        when(jwtUtil.generateAccessToken(mockUserDetails))
                .thenReturn("accessToken123");
        when(jwtUtil.generateRefreshToken(mockUserDetails))
                .thenReturn("refreshToken123");

        // When
        AuthDto.LoginResponse response = authService.login(request);

        // Then
        assertThat(response.getAccessToken()).isEqualTo("accessToken123");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken123");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUserInfo().getUsername()).isEqualTo("testuser");
        assertThat(response.getUserInfo().getNickname()).isEqualTo("테스트유저");

        // 호출 검증
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(accountRepository).findByUsername("testuser");
        verify(jwtUtil).generateAccessToken(mockUserDetails);
        verify(jwtUtil).generateRefreshToken(mockUserDetails);
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_BadCredentials() {
        // Given
        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When & Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        // 인증 실패 시 이후 로직이 실행되지 않음을 확인
        verify(accountRepository, never()).findByUsername(anyString());
        verify(jwtUtil, never()).generateAccessToken(any());
        verify(jwtUtil, never()).generateRefreshToken(any());
    }

    @Test
    @DisplayName("아이디 중복 확인 - 사용 가능")
    void checkUsername_Available() {
        // Given
        String username = "newuser";
        when(accountRepository.existsByUsername(username)).thenReturn(false);

        // When
        AuthDto.UsernameCheckResponse response = authService.checkUsername(username);

        // Then
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.isAvailable()).isTrue();
        assertThat(response.getMessage()).isEqualTo("사용 가능한 아이디입니다.");

        verify(accountRepository).existsByUsername(username);
    }

    @Test
    @DisplayName("아이디 중복 확인 - 이미 존재")
    void checkUsername_AlreadyExists() {
        // Given
        String username = "existuser";
        when(accountRepository.existsByUsername(username)).thenReturn(true);

        // When
        AuthDto.UsernameCheckResponse response = authService.checkUsername(username);

        // Then
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.isAvailable()).isFalse();
        assertThat(response.getMessage()).isEqualTo("이미 사용중인 아이디입니다.");

        verify(accountRepository).existsByUsername(username);
    }

    @Test
    @DisplayName("사용자 정보 조회 성공")
    void getUserInfo_Success() {
        // Given
        String username = "testuser";
        Account mockAccount = Account.builder()
                .id(1L) // BaseEntity의 id
                .username("testuser")
                .nickname("테스트유저")
                .location("서울")
                .password("encodedPassword")
                .isBlock(false)
                .build();

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(mockAccount));

        // When
        AuthDto.UserInfoResponse response = authService.getUserInfo(username);

        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getNickname()).isEqualTo("테스트유저");
        assertThat(response.getLocation()).isEqualTo("서울");
        assertThat(response.getMessage()).isEqualTo("로그인한 사용자입니다.");

        verify(accountRepository).findByUsername(username);
    }
}