package org.jpetto.meetpickback.group.members.controller;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.group.members.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/{groupId}/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /* 멤버 추가 */
    @PostMapping
    public void addMember() {

    }

    /* 멤버 강퇴 */

    /* 멤버 나가기 (강퇴랑 동일 서비스 사용) */

    /* 방장 위임 */

    /* 멤버 조회 */
}
