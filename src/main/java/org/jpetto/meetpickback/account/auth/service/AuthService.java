package org.jpetto.meetpickback.account.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpetto.meetpickback.account.auth.dto.AuthDto;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.account.accounts.repository.AccountRepository;
import org.jpetto.meetpickback.global.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.jpetto.meetpickback.global.utils.StringUtil.nvl;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDto.SignUpResponse signUp(AuthDto.SignUpRequest signUpRequest) {
        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Account newAccount = Account.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .location(nvl(signUpRequest.getLocation()))
                .isBlock(false)
                .build();

        accountRepository.save(newAccount);

        log.info("새로운 사용자 회원가입 완료. id: {},username: {} pw: {}, nickname: {}, location: {}, block: {}",
                newAccount.getId(), newAccount.getUsername(), newAccount.getNickname(), newAccount.getNickname(), newAccount.getLocation(), newAccount.isBlock());

        return AuthDto.SignUpResponse.builder()
                .id(newAccount.getId())
                .username(newAccount.getUsername())
                .nickname(newAccount.getNickname())
                .message("회원가입이 완료되었습니다.")
                .build();
    }

    public AuthDto.UsernameCheckResponse checkUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return AuthDto.UsernameCheckResponse.builder()
                    .username(username)
                    .available(false)
                    .message("아이디를 입력해주세요.")
                    .build();
        }

        if (username.length() < 6 || username.length() > 20) {
            return AuthDto.UsernameCheckResponse.builder()
                    .username(username)
                    .available(false)
                    .message("아이디는 6자 이상 20자 이하여야 합니다.")
                    .build();
        }

        boolean exists = accountRepository.existsByUsername(username);

        if (exists) {
            log.debug("아이디 중복 확인 - 이미 존재: {}", username);
            return AuthDto.UsernameCheckResponse.builder()
                    .username(username)
                    .available(false)
                    .message("이미 사용중인 아이디입니다.")
                    .build();
        } else {
            log.debug("아이디 중복 확인 - 사용 가능: {}", username);
            return AuthDto.UsernameCheckResponse.builder()
                    .username(username)
                    .available(true)
                    .message("사용 가능한 아이디입니다.")
                    .build();
        }
    }

    @Transactional
    public AuthDto.LoginResponse login(AuthDto.LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Account account = accountRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            log.info("로그인 성공 - 사용자: {}", loginRequest.getUsername());

            return AuthDto.LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .userInfo(AuthDto.LoginResponse.UserInfo.builder()
                            .id(account.getId())
                            .username(account.getUsername())
                            .nickname(account.getNickname())
                            .build())
                    .build();

        } catch (BadCredentialsException e) {
            log.warn("로그인 실패 - 잘못된 인증 정보: {}", loginRequest.getUsername());
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }

    public AuthDto.UserInfoResponse getUserInfo(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return AuthDto.UserInfoResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .nickname(account.getNickname())
                .location(account.getLocation())
                .message("로그인한 사용자입니다.")
                .build();
    }
}
