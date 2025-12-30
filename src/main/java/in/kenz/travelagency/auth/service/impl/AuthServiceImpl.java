package in.kenz.travelagency.auth.service.impl;

import in.kenz.travelagency.auth.dto.LoginRequest;
import in.kenz.travelagency.auth.dto.LoginResponse;
import in.kenz.travelagency.auth.dto.SignupFormRequestDTO;
import in.kenz.travelagency.auth.service.AuthService;
import in.kenz.travelagency.config.security.JwtUtil;
import in.kenz.travelagency.user.entity.User;
import in.kenz.travelagency.user.enums.UserProfileStatus;
import in.kenz.travelagency.user.enums.UserRole;
import in.kenz.travelagency.user.repository.UserRepository;
import in.kenz.travelagency.user.service.UserService;
import in.kenz.travelagency.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;



    @Override
    public void register(SignupFormRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileStatus(UserProfileStatus.ACTIVE)
                .roles(Set.of(UserRole.USER))
                .build();

        userRepository.save(user);
    }


    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

}