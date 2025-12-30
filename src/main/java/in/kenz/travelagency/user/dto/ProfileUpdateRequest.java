package in.kenz.travelagency.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileUpdateRequest {
    @NotBlank
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String mobileNumber;
    @NotBlank
    private String emergencyContactName;
    @NotBlank
    private String emergencyContactNumber;
}
