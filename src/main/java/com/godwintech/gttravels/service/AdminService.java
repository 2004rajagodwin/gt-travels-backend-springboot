package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.CreateOperatorRequest;
import com.godwintech.gttravels.entity.Role;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AccountStatus;
import com.godwintech.gttravels.enums.AuthProvider;
import com.godwintech.gttravels.enums.ERole;
import com.godwintech.gttravels.repository.RoleRepository;
import com.godwintech.gttravels.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createOperator(CreateOperatorRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role operatorRole = roleRepository.findByName(ERole.ROLE_OPERATOR)
                .orElseThrow(() -> new RuntimeException("ROLE_OPERATOR not found"));

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setAccountStatus(AccountStatus.ACTIVE);

        user.setRoles(Set.of(operatorRole));

        userRepository.save(user);

        return "Operator created successfully";
    }

}