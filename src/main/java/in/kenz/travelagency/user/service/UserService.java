package in.kenz.travelagency.user.service;

import in.kenz.travelagency.user.dto.ProfileUpdateRequest;
import in.kenz.travelagency.user.dto.UserSignupRequest;
import in.kenz.travelagency.user.dto.UserProfileResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
        void createUser(UserSignupRequest userSignupRequest);
        UserProfileResponse fetchProfile(UUID userId);
        void updateProfile(UUID userId, ProfileUpdateRequest profileUpdateRequest);
        void deleteProfile(UUID userId);
        List<UserProfileResponse> fetchAllUsers();
}
