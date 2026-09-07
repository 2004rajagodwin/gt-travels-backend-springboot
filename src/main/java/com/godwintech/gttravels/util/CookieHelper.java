package com.godwintech.gttravels.util;

import com.godwintech.gttravels.config.AuthProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public final class CookieHelper {

    public static final String REFRESH_COOKIE = "refresh_token";

    private CookieHelper() {
    }

    public static void setRefreshCookie(HttpServletResponse response,
                                       String rawToken,
                                       long maxAgeSeconds,
                                       AuthProperties authProperties) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE, rawToken)
                .httpOnly(true)
                .secure(authProperties.isCookieSecure())
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(maxAgeSeconds)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void clearRefreshCookie(HttpServletResponse response,
                                          AuthProperties authProperties) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(authProperties.isCookieSecure())
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static String extractRefreshToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (REFRESH_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
