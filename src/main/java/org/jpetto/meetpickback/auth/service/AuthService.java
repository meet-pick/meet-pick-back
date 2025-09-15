package org.jpetto.meetpickback.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpetto.meetpickback.auth.dto.AuthDto;
import org.jpetto.meetpickback.auth.entity.Account;
import org.jpetto.meetpickback.auth.repository.AccountRepository;
import org.jpetto.meetpickback.global.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

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

        log.info("새로운 사용자 회원가입 완료. id: {}, pw: {}, nickname: {}, location: {}, block: {}",
                newAccount.getUsername(), newAccount.getNickname(), newAccount.getNickname(), newAccount.getLocation(), newAccount.isBlock());

        return AuthDto.SignUpResponse.builder()
                .id(newAccount.getId())
                .username(newAccount.getUsername())
                .nickname(newAccount.getNickname())
                .message("회원가입이 완료되었습니다.")
                .build();
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }

}
