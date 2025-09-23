package org.jpetto.meetpickback.account.account.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.dto.AccountDto;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.account.service.AccountService;
import org.jpetto.meetpickback.account.auth.dto.AuthDto;
import org.jpetto.meetpickback.account.auth.service.AuthService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountDto.AccountMyPageResponse> getLoginUserAccount(@LoginUser Account loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        AccountDto.AccountMyPageResponse account = accountService.getMyAccounts(loginUser);

        return ResponseEntity.ok(account);
    }
}
