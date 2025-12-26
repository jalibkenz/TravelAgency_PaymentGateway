package in.kenz.travelagency.user.service;

import in.kenz.travelagency.user.dto.ChangePasswordDTO;
import in.kenz.travelagency.user.dto.UserDTO;

public interface UserService {

    UserDTO findByUsername(String username);

    void updateProfile(String username, UserDTO userDTO);

    void changePassword(String username, ChangePasswordDTO dto);
}
