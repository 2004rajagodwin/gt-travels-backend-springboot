package com.godwintech.gttravels.security;

import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AccountStatus;
import com.godwintech.gttravels.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        boolean locked = user.getLockedUntil() != null
                && user.getLockedUntil().isAfter(LocalDateTime.now());

        boolean disabled = !user.isEnabled()
                || user.getAccountStatus() == AccountStatus.DISABLED;

        String password = user.getPassword() != null ? user.getPassword() : "";

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                password,
                user.isEnabled() && user.getAccountStatus() != AccountStatus.DISABLED,
                true,
                true,
                !locked,
                authorities
        );
    }
}
