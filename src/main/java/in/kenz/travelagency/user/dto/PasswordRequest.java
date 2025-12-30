package in.kenz.travelagency.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PasswordRequest {

    @NotEmpty
    private String currentPassword;
    @NotEmpty
    private String newPassword;

}
