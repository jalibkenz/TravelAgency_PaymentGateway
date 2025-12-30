package in.kenz.travelagency.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserSignupRequest {
    private String username;
    private String email;
    private String password;
}
