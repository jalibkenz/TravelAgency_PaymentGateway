package in.kenz.travelagency.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupFormRequestDTO {

    private String username;
    private String email;
    private String password;
}