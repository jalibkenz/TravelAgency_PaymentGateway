package in.kenz.travelagency.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}