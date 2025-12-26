package in.kenz.travelagency.user.service.impl;

import in.kenz.travelagency.common.exception.DuplicateResourceException;
import in.kenz.travelagency.common.exception.InvalidCredentialsException;
import in.kenz.travelagency.common.exception.ResourceNotFoundException;
import in.kenz.travelagency.user.dto.ChangePasswordDTO;
import in.kenz.travelagency.user.dto.UserDTO;
import in.kenz.travelagency.user.entity.User;
import in.kenz.travelagency.user.repository.UserRepository;
import in.kenz.travelagency.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * @return userDTO
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    public UserDTO findByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("A User with such username not found"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        return dto;
    }


    /**
     * @throws ResourceNotFoundException a user pointed out with username is not found
     * @throws DuplicateResourceException   if the new email id provided to be updated already exist in database
     */
    @Override
    public void updateProfile(String username, UserDTO userDTO) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("A User with such username not found"));

        // Email change check
        if (!user.getEmail().equals(userDTO.getEmail())
                && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email already in DB");
        }

        user.setEmail(userDTO.getEmail());
        userRepository.save(user);
    }

    /**
     *
     * @throws ResourceNotFoundException if user is not found
     * @throws InvalidCredentialsException i
     */
    @Override
    public void changePassword(String username, ChangePasswordDTO dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("You provided wrong existing saved password!, so Fresh Password cannot be updated");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}