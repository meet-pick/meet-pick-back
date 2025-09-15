package org.jpetto.meetpickback.global.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCookieUtil {
    @Value("${jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookie.domain:}")
    private String cookieDomain;

    private static final String ACCESS_TOKEN_COOKIE = "accessToken";
    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    // 인증 쿠키 설정
    public void setAuthenticationCookies(HttpServletResponse response,
                                         String accessToken, String refreshToken) {
        setAccessTokenCookie(response, accessToken);
        setRefreshTokenCookie(response, refreshToken);
        log.debug("인증 쿠키가 설정되었습니다.");
    }

    // 인증 쿠키 삭제
    public void clearAuthenticationCookies(HttpServletResponse response) {
        clearCookie(response, ACCESS_TOKEN_COOKIE);
        clearCookie(response, REFRESH_TOKEN_COOKIE);
        log.debug("인증 쿠키가 삭제되었습니다.");
    }

    // 액세스 토큰만 갱신
    public void refreshAccessTokenCookie(HttpServletResponse response, String newAccessToken) {
        setAccessTokenCookie(response, newAccessToken);
        log.debug("액세스 토큰 쿠키가 갱신되었습니다.");
    }

    private void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = createSecureCookie(ACCESS_TOKEN_COOKIE, accessToken,
                (int) (accessExpiration / 1000));
        response.addCookie(cookie);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = createSecureCookie(REFRESH_TOKEN_COOKIE, refreshToken,
                (int) (refreshExpiration / 1000));
        response.addCookie(cookie);
    }

    private Cookie createSecureCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);        // XSS 방어
        cookie.setSecure(cookieSecure);  // HTTPS 전용 (운영환경)
        cookie.setPath("/");             // 전체 경로
        cookie.setMaxAge(maxAge);        // 만료 시간

        if (!cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain); // 도메인 설정
        }

        // SameSite 설정 (CSRF 방어)
        cookie.setAttribute("SameSite", "Strict");

        return cookie;
    }

    private void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }
}
