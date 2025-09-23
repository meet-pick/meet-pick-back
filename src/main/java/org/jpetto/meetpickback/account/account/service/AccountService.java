package org.jpetto.meetpickback.account.account.service;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.dto.AccountDto;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountDto.AccountMyPageResponse getMyAccounts(Account loginUser) {
        Account account = accountRepository.findByUsername(loginUser.getUsername()).orElseThrow(() -> new IllegalArgumentException("로그인 유저가 아님"));

        return AccountDto.AccountMyPageResponse.builder()
                .username(account.getUsername())
                .nickname(account.getNickname())
                .location(account.getLocation())
                .message("현재 로그인한 유저의 정보")
                .build();
    }
}
