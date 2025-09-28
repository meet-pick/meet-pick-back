package org.jpetto.meetpickback.group.member.controller;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.group.member.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
}
