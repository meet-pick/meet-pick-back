package org.jpetto.meetpickback.account.auth.service;

import org.jpetto.meetpickback.account.account.entity.Account;

import java.lang.reflect.Field;

/**
 * 테스트용 Account 엔티티 빌더
 * JPA @GeneratedValue를 시뮬레이션하기 위한 헬퍼 클래스
 */
public class TestAccountBuilder {

    /**
     * ID가 설정된 Account 객체를 생성
     * 리플렉션을 사용해서 BaseEntity의 private id 필드에 값을 설정
     */
    public static Account createAccountWithId(Long id, String username, String password,
                                              String nickname, String location, boolean isBlock) {
        try {
            Account account = Account.builder()
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .location(location)
                    .isBlock(isBlock)
                    .build();

            // 리플렉션으로 BaseEntity의 id 필드에 접근
            Field idField = account.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(account, id);

            // 디버깅: ID가 제대로 설정되었는지 확인
            System.out.println("=== TestAccountBuilder 디버깅 ===");
            System.out.println("설정한 ID: " + id);
            System.out.println("Account의 실제 ID: " + account.getId());
            System.out.println("Account Username: " + account.getUsername());
            System.out.println("Account Nickname: " + account.getNickname());

            return account;
        } catch (Exception e) {
            System.err.println("TestAccountBuilder 에러: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("테스트 Account 생성 실패", e);
        }
    }

    /**
     * 기본값으로 Account 생성 (ID = 1L)
     */
    public static Account createDefaultAccount() {
        return createAccountWithId(1L, "testuser", "encodedPassword",
                "테스트유저", "서울", false);
    }

    /**
     * 회원가입용 Account 생성
     */
    public static Account createSignUpAccount(String username, String password,
                                              String nickname, String location) {
        return createAccountWithId(1L, username, password, nickname, location, false);
    }
}