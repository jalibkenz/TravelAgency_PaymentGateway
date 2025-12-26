package in.kenz.travelagency.auth.service;

import in.kenz.travelagency.auth.dto.RegisterRequest;
import in.kenz.travelagency.common.exception.DuplicateResourceException;
import in.kenz.travelagency.user.entity.User;
import in.kenz.travelagency.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //inbuilt interface

    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of("ROLE_USER"))
                .build();

        userRepository.save(user);
    }
}