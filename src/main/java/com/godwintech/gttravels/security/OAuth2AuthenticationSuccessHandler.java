package com.godwintech.gttravels.security;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final AuthProperties authProperties;

    public OAuth2AuthenticationSuccessHandler(@Lazy AuthService authService,
                                              AuthProperties authProperties) {
        this.authService = authService;
        this.authProperties = authProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String googleId = oauthUser.getAttribute("sub");
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        if (googleId == null || email == null) {
            response.sendRedirect(authProperties.getFrontendUrl() + "/login?error=oauth_failed");
            return;
        }

        try {
            var user = authService.processGoogleUser(googleId, email, name, picture);

            boolean isCustomer = user.getRoles().stream()
                    .anyMatch(role -> role.getName().name().equals("ROLE_CUSTOMER"));

            if (!isCustomer) {
                response.sendRedirect(authProperties.getFrontendUrl() + "/login?error=invalid_role");
                return;
            }

            AuthService.AuthResult result = authService.issueTokensForUser(user, request, response);

            String redirectUrl = UriComponentsBuilder
                    .fromUriString(authProperties.getFrontendUrl() + "/auth/google/callback")
                    .queryParam("accessToken", URLEncoder.encode(
                            result.getAuthResponse().getAccessToken(), StandardCharsets.UTF_8))
                    .build(true)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(authProperties.getFrontendUrl() + "/login?error=oauth_failed");
        }
    }
}
