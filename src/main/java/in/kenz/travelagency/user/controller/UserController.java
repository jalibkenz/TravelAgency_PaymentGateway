package in.kenz.travelagency.user.controller;

import in.kenz.travelagency.user.dto.ProfileUpdateRequest;
import in.kenz.travelagency.user.dto.UserSignupRequest;
import in.kenz.travelagency.user.dto.UserProfileResponse;
import in.kenz.travelagency.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Create User (Signup)
     */
    @PostMapping
    public ResponseEntity<Void> createUser(
            @Valid @RequestBody UserSignupRequest request
    ) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Fetch logged-in user's profile
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> fetchMyProfile(
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(userService.fetchProfile(userId));
    }

    /**
     * Update logged-in user's profile
     */
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        userService.updateProfile(userId, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete logged-in user's profile
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        userService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetch all users (Admin use-case)
     */
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> fetchAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }
}
