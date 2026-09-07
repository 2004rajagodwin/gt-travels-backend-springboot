package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.security.CustomUserDetailsService;
import com.godwintech.gttravels.dto.AuthResponse;
import com.godwintech.gttravels.dto.ChangePasswordRequest;
import com.godwintech.gttravels.dto.LoginRequest;
import com.godwintech.gttravels.dto.RefreshTokenResult;
import com.godwintech.gttravels.dto.RegisterRequest;
import com.godwintech.gttravels.dto.SessionResponse;
import com.godwintech.gttravels.entity.Role;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AccountStatus;
import com.godwintech.gttravels.enums.AuthProvider;
import com.godwintech.gttravels.enums.ERole;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.exception.EmailNotVerifiedException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.RoleRepository;
import com.godwintech.gttravels.repository.UserRepository;
import com.godwintech.gttravels.security.JwtTokenProvider;
import com.godwintech.gttravels.repository.UserRepository;
import com.godwintech.gttravels.util.PasswordValidator;
import com.godwintech.gttravels.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;
    private final AccountSecurityService accountSecurityService;
    private final AuthEmailService authEmailService;
    private final AuthProperties authProperties;
    private final CustomUserDetailsService customUserDetailsService;

  public AuthService(UserRepository userRepo,
                       RoleRepository roleRepo,
                       PasswordEncoder encoder,
                       AuthenticationManager authManager,
                       JwtTokenProvider jwtProvider,
                       RefreshTokenService refreshTokenService,
                       EmailVerificationService emailVerificationService,
                       PasswordResetService passwordResetService,
                       AccountSecurityService accountSecurityService,
                       AuthEmailService authEmailService,
                       AuthProperties authProperties,
                       CustomUserDetailsService customUserDetailsService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.emailVerificationService = emailVerificationService;
        this.passwordResetService = passwordResetService;
        this.accountSecurityService = accountSecurityService;
        this.authEmailService = authEmailService;
        this.authProperties = authProperties;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Transactional
    public void register(RegisterRequest request) {
        PasswordValidator.validate(request.getPassword());

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already in use");
        }
        if (request.getPhone() != null
                && !request.getPhone().isBlank()
                && userRepo.existsByPhone(request.getPhone())) {
            throw new ConflictException("Phone number already in use");
        }

        Role customerRole = roleRepo.findByName(ERole.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setEmailVerified(false);
        user.setEnabled(false);
        user.setAccountStatus(AccountStatus.ACTIVE);

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);
        user.setRoles(roles);

        User saved = userRepo.save(user);
        emailVerificationService.createAndSendVerificationToken(saved);
    }

    public AuthResult login(LoginRequest request, HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (user.getAuthProvider() == AuthProvider.LOCAL && !user.isEmailVerified()) {
            throw new EmailNotVerifiedException("EMAIL_NOT_VERIFIED");
        }

        accountSecurityService.ensureCanAuthenticate(user);

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            accountSecurityService.resetFailedLogin(user);
            return issueTokens(authentication, user, httpRequest, httpResponse);
        } catch (BadCredentialsException ex) {
            accountSecurityService.recordFailedLogin(user);
            throw ex;
        }
    }

 public AuthResult refreshAccessToken(String rawRefreshToken,
                                         HttpServletRequest httpRequest,
                                         HttpServletResponse httpResponse) {
        RefreshTokenResult rotated = refreshTokenService.rotateRefreshToken(
                rawRefreshToken,
                httpRequest.getHeader("User-Agent"),
                httpRequest.getRemoteAddr());
        User user = refreshTokenService.findUserByRawToken(rotated.getRawToken());

        var userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        String accessToken = jwtProvider.generateToken(authentication);
        com.godwintech.gttravels.util.CookieHelper.setRefreshCookie(
                httpResponse, rotated.getRawToken(), rotated.getMaxAgeSeconds(), authProperties);
        return new AuthResult(
                new AuthResponse(
                        accessToken,
                        authProperties.getAccessExpirationMs() / 1000,
                        user.getEmail(),
                        user.getRoles().stream().map(r -> r.getName().name()).toList()),
                rotated);
    }

    public void logout(String rawRefreshToken, HttpServletResponse httpResponse) {
        refreshTokenService.revokeToken(rawRefreshToken);
        com.godwintech.gttravels.util.CookieHelper.clearRefreshCookie(httpResponse, authProperties);
    }

    public void logoutAll(User user, HttpServletResponse httpResponse) {
        refreshTokenService.revokeAllForUser(user.getId());
        com.godwintech.gttravels.util.CookieHelper.clearRefreshCookie(httpResponse, authProperties);
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (user.getAuthProvider() == AuthProvider.GOOGLE && user.getPassword() == null) {
            throw new BadRequestException("Google accounts must use Google sign-in");
        }
        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        PasswordValidator.validate(request.getNewPassword());
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepo.save(user);
        refreshTokenService.revokeAllForUser(user.getId());
        authEmailService.sendPasswordChangedEmail(user);
    }

    public List<SessionResponse> getSessions(User user, String currentRefreshToken) {
        String hash = currentRefreshToken == null ? null : TokenUtils.hashToken(currentRefreshToken);
        return refreshTokenService.getActiveSessions(user, hash);
    }

    @Transactional
    public User processGoogleUser(String googleId, String email, String name, String picture) {
        User user = userRepo.findByGoogleId(googleId)
                .or(() -> userRepo.findByEmail(email))
                .map(existing -> linkGoogleAccount(existing, googleId, picture))
                .orElseGet(() -> createGoogleUser(googleId, email, name, picture));
        user.getRoles().size();
        return user;
    }

    public AuthResult issueTokensForUser(User user,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
accountSecurityService.ensureCanAuthenticate(user);

var userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

Authentication authentication = new UsernamePasswordAuthenticationToken(
userDetails,
null,
userDetails.getAuthorities());

return issueTokens(authentication, user, httpRequest, httpResponse);
}
    
    

    private AuthResult issueTokens(Authentication authentication,
                                   User user,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {
        String accessToken = jwtProvider.generateToken(authentication);
        RefreshTokenResult refresh = refreshTokenService.createRefreshToken(
                user,
                httpRequest.getHeader("User-Agent"),
                httpRequest.getRemoteAddr());

        com.godwintech.gttravels.util.CookieHelper.setRefreshCookie(
                httpResponse, refresh.getRawToken(), refresh.getMaxAgeSeconds(), authProperties);

        AuthResponse response = new AuthResponse(
                accessToken,
                authProperties.getAccessExpirationMs() / 1000,
                user.getEmail(),
                user.getRoles().stream().map(r -> r.getName().name()).toList());

        return new AuthResult(response, refresh);
    }

    private User linkGoogleAccount(User user, String googleId, String picture) {
        accountSecurityService.ensureCanAuthenticate(user);
        user.setGoogleId(googleId);
        if (user.getAuthProvider() == AuthProvider.LOCAL) {
            user.setAuthProvider(AuthProvider.GOOGLE);
        }
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setAccountStatus(AccountStatus.ACTIVE);
        if (picture != null) {
            user.setProfileImage(picture);
        }
        return userRepo.save(user);
    }

    private User createGoogleUser(String googleId, String email, String name, String picture) {
        Role customerRole = roleRepo.findByName(ERole.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        User user = new User();
        user.setGoogleId(googleId);
        user.setEmail(email);
        user.setName(name);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setProfileImage(picture);

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);
        user.setRoles(roles);

        return userRepo.save(user);
    }

    public static class AuthResult {
        private final AuthResponse authResponse;
        private final RefreshTokenResult refreshTokenResult;

        public AuthResult(AuthResponse authResponse, RefreshTokenResult refreshTokenResult) {
            this.authResponse = authResponse;
            this.refreshTokenResult = refreshTokenResult;
        }

        public AuthResponse getAuthResponse() {
            return authResponse;
        }

        public RefreshTokenResult getRefreshTokenResult() {
            return refreshTokenResult;
        }
    }
}
