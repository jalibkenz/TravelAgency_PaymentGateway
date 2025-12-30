package in.kenz.travelagency.user.service.impl;

import in.kenz.travelagency.common.exception.DuplicateResourceException;
import in.kenz.travelagency.common.exception.ResourceNotFoundException;
import in.kenz.travelagency.user.dto.ProfileUpdateRequest;
import in.kenz.travelagency.user.dto.UserSignupRequest;
import in.kenz.travelagency.user.dto.UserProfileResponse;
import in.kenz.travelagency.user.entity.User;
import in.kenz.travelagency.user.enums.UserRole;
import in.kenz.travelagency.user.repository.UserRepository;
import in.kenz.travelagency.user.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void createUser(UserSignupRequest userSignupRequest) {

        //ensuring there doesn't already exist such username
        if (userRepository.existsByUsername(userSignupRequest.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        //ensuring there doesn't already exist such email
        if (userRepository.existsByEmail(userSignupRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }


        //saving fresh username and email
        //saving role to Entity owned roles
        User user = User.builder()
                .username(userSignupRequest.getUsername())
                .email(userSignupRequest.getEmail())
                .password(userSignupRequest.getPassword())
                .build();
        user.addRole(UserRole.USER);
        userRepository.save(user); //as it this transient object needs to be persisted


    }


    @Override
    public UserProfileResponse fetchProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User could not be fetched from DB"));

        return UserProfileResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .build();
    }


    @Transactional
    @Override
    public void updateProfile(UUID userId, ProfileUpdateRequest profileUpdateRequest) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User could not be fetched from DB")
        );


        //newly provided username should not exist already
        if (profileUpdateRequest.getUsername() != null && !profileUpdateRequest.getUsername().equals(user.getUsername())) {
            //NOT username already same as existing one used by user ( Allow RETRY during RESPONSE loss)

            if (userRepository.existsByUsernameAndIdNot(profileUpdateRequest.getUsername(), userId)) { //username already used by another user
                throw new DuplicateResourceException("Username already in use");
            }

            user.setUsername(profileUpdateRequest.getUsername());

        }
        if (profileUpdateRequest.getEmail() != null && !profileUpdateRequest.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmailAndIdNot(profileUpdateRequest.getEmail(), userId)) { //email already used by another user {
                throw new DuplicateResourceException("Email already in use");
            }

            user.setEmail(profileUpdateRequest.getEmail());

        }
        if (profileUpdateRequest.getMobileNumber() != null) {
            user.setMobileNumber(profileUpdateRequest.getMobileNumber());

        }
        if (profileUpdateRequest.getEmergencyContactName() != null) {
            user.setEmergencyContactName(profileUpdateRequest.getEmergencyContactName());

        }
        if (profileUpdateRequest.getEmergencyContactNumber() != null) {
            user.setEmergencyContactNumber(profileUpdateRequest.getEmergencyContactNumber());

        }


    }


    /**
     * 404 Not Found if missing
     * 204 No Content if deleted
     */
    @Override
    @Transactional
    public void deleteProfile(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }


    @Override
    public List<UserProfileResponse> fetchAllUsers() {
        return userRepository.findAllBy();
    }
}
