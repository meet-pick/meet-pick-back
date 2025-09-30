package org.jpetto.meetpickback.account.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.accounts.dto.AccountDto;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.account.accounts.service.AccountService;
import org.jpetto.meetpickback.global.loginUser.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
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
